package com.inventory.tracker.service;

import com.inventory.tracker.dto.WarehouseRequestDto;
import com.inventory.tracker.dto.WarehouseResponseDto;
import com.inventory.tracker.model.Warehouse;
import com.inventory.tracker.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public List<WarehouseResponseDto> getAllWarehouses() {
        log.info("Fetching all warehouses");
        return warehouseRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public WarehouseResponseDto getWarehouseById(Long id) {
        log.info("Fetching warehouse by id: {}", id);
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));
        return mapToDto(warehouse);
    }

    @Transactional
    public WarehouseResponseDto createWarehouse(WarehouseRequestDto request) {
        log.info("Creating new warehouse: {}", request.getName());
        Warehouse warehouse = Warehouse.builder()
                .name(request.getName())
                .location(request.getLocation())
                .capacity(request.getCapacity())
                .build();
        return mapToDto(warehouseRepository.save(warehouse));
    }

    @Transactional
    public WarehouseResponseDto updateWarehouse(Long id, WarehouseRequestDto request) {
        log.info("Updating warehouse id: {}", id);
        Warehouse warehouse = warehouseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse not found"));
        
        warehouse.setName(request.getName());
        warehouse.setLocation(request.getLocation());
        warehouse.setCapacity(request.getCapacity());
        
        return mapToDto(warehouseRepository.save(warehouse));
    }

    @Transactional
    public void deleteWarehouse(Long id) {
        log.info("Deleting warehouse id: {}", id);
        warehouseRepository.deleteById(id);
    }

    private WarehouseResponseDto mapToDto(Warehouse warehouse) {
        return WarehouseResponseDto.builder()
                .id(warehouse.getId())
                .name(warehouse.getName())
                .location(warehouse.getLocation())
                .capacity(warehouse.getCapacity())
                .createdAt(warehouse.getCreatedAt())
                .build();
    }
}
