package com.inventory.tracker.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TransactionResponseDto {
    private Long id;
    private Long inventoryId;
    private String transactionType;
    private Integer quantity;
    private String reason;
    private Long createdBy;
    private LocalDateTime createdAt;
}
