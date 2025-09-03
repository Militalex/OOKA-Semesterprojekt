package com.github.militalex.messaging;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    /**
     * All Kafka Topics
     */
    public static final String FRONTEND_TOPIC = "frontend";
    public static final String ENG_MNG_MICROSERVICE_TOPIC = "engMngMicroservice";

    /**
     * All Kafka Partitions for the FRONTEND topic.
     */
    public static class FrontendPartitions {
        public static final int PARTITION_COUNT = 4;
        public static final int PING = 0;
        public static final int ALGORITHM_STATES = 1;
        public static final int REQUEST_ANALYSIS = 2;
        public static final int ALGORITHM_RESULTS = 3;
    }

    /**
     * All Kafka Partitions for the ENG_MNG_MICROSERVICE topic.
     */
    public static class EngMngPartitions {
        public static final int PARTITION_COUNT = 2;
        public static final int REQUEST_RESULT = 0;
        public static final int ALGORITHM_RESULTS = 1;
    }

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Bean
    public KafkaAdmin kafkaAdmin(){
        Map<String, Object> configs = Map.of(
                AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress
        );
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic frontendTopic() {
        return new NewTopic(FRONTEND_TOPIC, FrontendPartitions.PARTITION_COUNT, (short) 1);
    }

    @Bean
    public NewTopic microserviceChoreoTopic() {
        return new NewTopic(ENG_MNG_MICROSERVICE_TOPIC, EngMngPartitions.PARTITION_COUNT, (short) 1);
    }
}
