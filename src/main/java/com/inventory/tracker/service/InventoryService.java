package com.inventory.tracker.service;

import com.inventory.tracker.exception.ResourceNotFoundException;
import com.inventory.tracker.exception.InsufficientStockException;

import com.inventory.tracker.dto.InventoryRequestDto;
import com.inventory.tracker.dto.InventoryResponseDto;
import com.inventory.tracker.kafka.producer.InventoryEventProducer;
import com.inventory.tracker.model.Inventory;
import com.inventory.tracker.model.Product;
import com.inventory.tracker.model.Warehouse;
import com.inventory.tracker.repository.InventoryRepository;
import com.inventory.tracker.repository.ProductRepository;
import com.inventory.tracker.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final InventoryEventProducer eventProducer;

    public List<InventoryResponseDto> getAllInventory() {
        log.info("Fetching all inventory");
        return inventoryRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public InventoryResponseDto getInventoryDtoById(Long id) {
        log.info("Fetching inventory by id: {}", id);
        return mapToDto(getInventoryById(id));
    }

    public List<InventoryResponseDto> getInventoryByWarehouse(Long warehouseId) {
        log.info("Fetching inventory for warehouse: {}", warehouseId);
        return inventoryRepository.findByWarehouseId(warehouseId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<InventoryResponseDto> getInventoryByProduct(Long productId) {
        log.info("Fetching inventory for product: {}", productId);
        return inventoryRepository.findByProductId(productId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public InventoryResponseDto createInventory(InventoryRequestDto request) {
        log.info("Creating new inventory for warehouse: {} product: {}", request.getWarehouseId(), request.getProductId());
        
        Warehouse warehouse = warehouseRepository.getReferenceById(request.getWarehouseId());
        Product product = productRepository.getReferenceById(request.getProductId());

        Inventory inventory = Inventory.builder()
                .warehouse(warehouse)
                .product(product)
                .quantity(request.getQuantity())
                .minThreshold(request.getMinThreshold())
                .build();
        return mapToDto(inventoryRepository.save(inventory));
    }

    @Transactional
    public InventoryResponseDto updateInventory(Long id, InventoryRequestDto request) {
        log.info("Updating inventory id: {}", id);
        Inventory inventory = getInventoryById(id);
        
        Warehouse warehouse = warehouseRepository.getReferenceById(request.getWarehouseId());
        Product product = productRepository.getReferenceById(request.getProductId());
        
        inventory.setWarehouse(warehouse);
        inventory.setProduct(product);
        inventory.setQuantity(request.getQuantity());
        inventory.setMinThreshold(request.getMinThreshold());
        
        return mapToDto(inventoryRepository.save(inventory));
    }

    @Cacheable(value = "inventory", key = "#id")
    public Inventory getInventoryById(Long id) {
        log.info("Fetching inventory from DB for ID: {}", id);
        return inventoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Inventory not found with ID: " + id));
    }

    @Transactional
    @CachePut(value = "inventory", key = "#id")
    public Inventory updateInventoryQuantity(Long id, Integer quantityChange) {
        log.info("Updating inventory quantity for id: {}, change: {}", id, quantityChange);
        Inventory inventory = getInventoryById(id);
        
        int newQuantity = inventory.getQuantity() + quantityChange;
        if (newQuantity < 0) {
            log.error("Insufficient stock for inventory id: {}", id);
            throw new InsufficientStockException("Insufficient stock. Current: " + inventory.getQuantity() + ", Requested reduction: " + Math.abs(quantityChange));
        }
        
        inventory.setQuantity(newQuantity);
        Inventory updatedInventory = inventoryRepository.save(inventory);
        
        // Produce Kafka event
        eventProducer.sendInventoryUpdateEvent(id, newQuantity);
        
        // Check threshold and produce alert if needed
        if (newQuantity <= inventory.getMinThreshold()) {
            log.warn("Stock alert generated for inventory id: {}", id);
            eventProducer.sendStockAlertEvent(id, newQuantity, inventory.getMinThreshold());
        }
        
        return updatedInventory;
    }

    public InventoryResponseDto patchInventoryQuantity(Long id, Integer quantity) {
        log.info("Patching inventory quantity for id: {} to: {}", id, quantity);
        Inventory inventory = getInventoryById(id);
        // Calculate the difference to reuse existing logic
        int diff = quantity - inventory.getQuantity();
        return mapToDto(updateInventoryQuantity(id, diff));
    }

    private InventoryResponseDto mapToDto(Inventory inventory) {
        return InventoryResponseDto.builder()
                .id(inventory.getId())
                .warehouseId(inventory.getWarehouse() != null ? inventory.getWarehouse().getId() : null)
                .productId(inventory.getProduct() != null ? inventory.getProduct().getId() : null)
                .quantity(inventory.getQuantity())
                .minThreshold(inventory.getMinThreshold())
                .updatedAt(inventory.getUpdatedAt())
                .build();
    }
}
