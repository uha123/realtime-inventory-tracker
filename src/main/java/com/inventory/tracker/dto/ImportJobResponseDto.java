package com.inventory.tracker.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ImportJobResponseDto {
    private Long id;
    private String fileName;
    private String status;
    private Integer totalRecords;
    private Integer processed;
    private Integer failed;
    private LocalDateTime createdAt;
}
