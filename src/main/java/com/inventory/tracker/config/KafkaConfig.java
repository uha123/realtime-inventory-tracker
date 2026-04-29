package com.inventory.tracker.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic inventoryUpdateTopic() {
        return TopicBuilder.name("inventory.update")
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic stockAlertTopic() {
        return TopicBuilder.name("stock.alert")
                .partitions(3)
                .replicas(1)
                .build();
    }
    
    @Bean
    public NewTopic importEventsTopic() {
        return TopicBuilder.name("import.events")
                .partitions(3)
                .replicas(1)
                .build();
    }
}
