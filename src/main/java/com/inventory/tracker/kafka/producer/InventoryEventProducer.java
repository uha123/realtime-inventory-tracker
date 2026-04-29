package com.inventory.tracker.kafka.producer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class InventoryEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendInventoryUpdateEvent(Long inventoryId, Integer newQuantity) {
        log.info("Sending inventory update event for ID: {} with quantity: {}", inventoryId, newQuantity);
        kafkaTemplate.send("inventory.update", String.valueOf(inventoryId), Map.of(
                "inventoryId", inventoryId,
                "quantity", newQuantity
        ));
    }

    public void sendStockAlertEvent(Long inventoryId, Integer currentQuantity, Integer minThreshold) {
        log.warn("Sending stock alert for inventory ID: {} (Quantity: {}, Threshold: {})", inventoryId, currentQuantity, minThreshold);
        kafkaTemplate.send("stock.alert", String.valueOf(inventoryId), Map.of(
                "inventoryId", inventoryId,
                "currentQuantity", currentQuantity,
                "minThreshold", minThreshold
        ));
    }
}
