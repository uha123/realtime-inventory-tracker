package com.inventory.tracker.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WarehouseResponseDto {
    private Long id;
    private String name;
    private String location;
    private Integer capacity;
    private LocalDateTime createdAt;
}
