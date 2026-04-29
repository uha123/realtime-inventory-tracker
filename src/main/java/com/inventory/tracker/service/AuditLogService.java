package com.inventory.tracker.service;

import com.inventory.tracker.dto.AuditLogResponseDto;
import com.inventory.tracker.model.AuditLog;
import com.inventory.tracker.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public List<AuditLogResponseDto> getAllAuditLogs() {
        log.info("Fetching all audit logs");
        return auditLogRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public AuditLogResponseDto getAuditLogById(Long id) {
        log.info("Fetching audit log by id: {}", id);
        AuditLog logEntity = auditLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit log not found"));
        return mapToDto(logEntity);
    }

    public List<AuditLogResponseDto> getAuditLogsByEntity(String entityType, Long entityId) {
        log.info("Fetching audit logs for entity: {} with id: {}", entityType, entityId);
        return auditLogRepository.findByEntityTypeAndEntityId(entityType, entityId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private AuditLogResponseDto mapToDto(AuditLog logEntity) {
        return AuditLogResponseDto.builder()
                .id(logEntity.getId())
                .entityType(logEntity.getEntityType())
                .entityId(logEntity.getEntityId())
                .action(logEntity.getAction())
                .oldValue(logEntity.getOldValue())
                .newValue(logEntity.getNewValue())
                .performedBy(logEntity.getPerformedBy() != null ? logEntity.getPerformedBy().getId() : null)
                .createdAt(logEntity.getCreatedAt())
                .build();
    }
}
