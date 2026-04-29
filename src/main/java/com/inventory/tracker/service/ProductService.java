package com.inventory.tracker.service;

import com.inventory.tracker.dto.ProductRequestDto;
import com.inventory.tracker.dto.ProductResponseDto;
import com.inventory.tracker.model.Product;
import com.inventory.tracker.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponseDto> getAllProducts() {
        log.info("Fetching all products");
        return productRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ProductResponseDto getProductById(Long id) {
        log.info("Fetching product by id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToDto(product);
    }

    public ProductResponseDto getProductBySku(String sku) {
        log.info("Fetching product by sku: {}", sku);
        // Assuming findBySku exists in ProductRepository
        Product product = productRepository.findBySku(sku)
                .orElseThrow(() -> new RuntimeException("Product not found with SKU: " + sku));
        return mapToDto(product);
    }

    @Transactional
    public ProductResponseDto createProduct(ProductRequestDto request) {
        log.info("Creating new product with sku: {}", request.getSku());
        Product product = Product.builder()
                .sku(request.getSku())
                .name(request.getName())
                .category(request.getCategory())
                .unitPrice(request.getUnitPrice())
                .build();
        return mapToDto(productRepository.save(product));
    }

    @Transactional
    public ProductResponseDto updateProduct(Long id, ProductRequestDto request) {
        log.info("Updating product id: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        product.setSku(request.getSku());
        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setUnitPrice(request.getUnitPrice());
        
        return mapToDto(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(Long id) {
        log.info("Deleting product id: {}", id);
        productRepository.deleteById(id);
    }

    private ProductResponseDto mapToDto(Product product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .sku(product.getSku())
                .name(product.getName())
                .category(product.getCategory())
                .unitPrice(product.getUnitPrice())
                .createdAt(product.getCreatedAt())
                .build();
    }
}
