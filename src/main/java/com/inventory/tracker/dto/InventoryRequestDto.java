package com.inventory.tracker.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class InventoryRequestDto {

    @NotNull(message = "Warehouse ID cannot be null")
    private Long warehouseId;

    @NotNull(message = "Product ID cannot be null")
    private Long productId;

    @NotNull(message = "Quantity cannot be null")
    @PositiveOrZero(message = "Quantity must be zero or greater")
    private Integer quantity;

    @NotNull(message = "Minimum threshold cannot be null")
    @PositiveOrZero(message = "Minimum threshold must be zero or greater")
    private Integer minThreshold;
}
