package com.inventory.tracker.kafka.consumer;

import com.inventory.tracker.model.AuditLog;
import com.inventory.tracker.model.User;
import com.inventory.tracker.repository.AuditLogRepository;
import com.inventory.tracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuditLogConsumer {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    @KafkaListener(topics = {"inventory.update", "stock.alert", "import.events"}, groupId = "audit-log-group")
    public void consumeEvent(Map<String, Object> eventPayload) {
        log.info("Consumed event for audit log: {}", eventPayload);
        
        // Example implementation of parsing event and saving audit log
        // In a real scenario, the payload would be a dedicated Event class containing the userId and entity details
        
        /* 
        User systemUser = userRepository.findByEmail("system@inventory.com").orElse(null);
        if (systemUser != null) {
            AuditLog auditLog = AuditLog.builder()
                    .entityType("INVENTORY_EVENT")
                    .entityId(Long.valueOf(eventPayload.get("inventoryId").toString()))
                    .action("EVENT_TRIGGERED")
                    .newValue(eventPayload.toString())
                    .performedBy(systemUser)
                    .build();
            auditLogRepository.save(auditLog);
        }
        */
    }
}
