package com.inventory.tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class TransactionRequestDto {

    @NotNull(message = "Inventory ID cannot be null")
    private Long inventoryId;

    @NotBlank(message = "Transaction type cannot be blank")
    private String transactionType; // IN, OUT, ADJUST

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    @NotBlank(message = "Reason cannot be blank")
    private String reason;

    @NotNull(message = "Created by user ID cannot be null")
    private Long createdBy;
}
