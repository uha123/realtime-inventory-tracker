package com.inventory.tracker.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductResponseDto {
    private Long id;
    private String sku;
    private String name;
    private String category;
    private BigDecimal unitPrice;
    private LocalDateTime createdAt;
}
