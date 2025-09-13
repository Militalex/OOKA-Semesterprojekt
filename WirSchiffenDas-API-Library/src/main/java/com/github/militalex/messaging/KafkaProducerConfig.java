package com.github.militalex.messaging;

import com.github.militalex.module.AlgorithmState;
import com.github.militalex.module.util.AlgorithmResult;
import com.github.militalex.module.util.OptionalEquipment;
import com.github.militalex.messaging.util.NullableInteger;
import com.github.militalex.messaging.util.Pair;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    /**
     * Frontend kann Microservices anpingen.
     */
    @Bean
    public ProducerFactory<Integer, String> pingProducerFactory() {
        Map<String, Object> configProps = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
        );

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<Integer, String> kafkaPingTemplate() {
        return new KafkaTemplate<>(pingProducerFactory());
    }

    /**
     * Microservices senden AlgorithmState an den Frontend-Service.
     */
    @Bean
    public ProducerFactory<NullableInteger, Pair<OptionalEquipment, AlgorithmState>> algorithmStatesProducerFactory() {
        Map<String, Object> configProps = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<NullableInteger, Pair<OptionalEquipment, AlgorithmState>> kafkaAlgorithmStatesTemplate() {
        return new KafkaTemplate<>(algorithmStatesProducerFactory());
    }

    /**
     * Frontend sendet die Analyseanfragen an die Microservices mit einem leeren AlgorithmResult Objekt
     */
    @Bean
    public ProducerFactory<Integer, AlgorithmResult> requestAnalysisProducerFactory(){
        Map<String, Object> configProps = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<Integer, AlgorithmResult> kafkaRequestAnalysisTemplate(){
        return new KafkaTemplate<>(requestAnalysisProducerFactory());
    }

    /**
     * Microservices senden Analyseergebnisse an den Frontend-Service.
     */
    @Bean
    public ProducerFactory<Integer, AlgorithmResult> frontendAlgoResultProducerFactory() {
        Map<String, Object> configProps = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<Integer, AlgorithmResult> kafkaFrontendAlgoResultsTemplate() {
        return new KafkaTemplate<>(frontendAlgoResultProducerFactory());
    }

    /**
     * Der Engine-Management-Microservice kann Analyseanfragen an die Microservices versenden.
     */
    @Bean
    public ProducerFactory<Integer, String> engMngRequestAnalysisProducerFactory(){
        Map<String, Object> configProps = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class
        );

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<Integer, String> kafkaEngMngRequestAnalysisTemplate(){
        return new KafkaTemplate<>(engMngRequestAnalysisProducerFactory());
    }

    /**
     * Microservices senden auf Anfrage ihr Ergebnis an den Engine-Management-Microservice
     */
    @Bean
    public ProducerFactory<Integer, AlgorithmResult> engMngAlgoResultProducerFactory() {
        Map<String, Object> configProps = Map.of(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class,
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
        );

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<Integer, AlgorithmResult> kafkaEngMngAlgoResultsTemplate() {
        return new KafkaTemplate<>(engMngAlgoResultProducerFactory());
    }
}
