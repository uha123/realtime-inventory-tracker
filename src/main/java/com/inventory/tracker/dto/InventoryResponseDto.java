package com.inventory.tracker.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class InventoryResponseDto {
    private Long id;
    private Long warehouseId;
    private Long productId;
    private Integer quantity;
    private Integer minThreshold;
    private LocalDateTime updatedAt;
}
