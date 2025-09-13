package com.github.militalex.messaging;

import com.github.militalex.messaging.util.NullableInteger;
import com.github.militalex.messaging.util.Pair;
import com.github.militalex.messaging.util.PairDeserializer;
import com.github.militalex.module.AlgorithmState;
import com.github.militalex.module.util.AlgorithmResult;
import com.github.militalex.module.util.OptionalEquipment;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    /**
     * Microservices können einen Ping von dem Frontend erhalten.
     */
    @Bean
    public ConsumerFactory<Integer, String> pingConsumerFactory() {
        Map<String, Object> configProps = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class
        );
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Integer, String> pingKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(pingConsumerFactory());
        return factory;
    }

    /**
     * Microservices können den Status ihrer Algorithmen an das Frontend senden.
     */
    @Bean
    public ConsumerFactory<NullableInteger, Pair<OptionalEquipment, AlgorithmState>> algorithmStatesConsumerFactory() {
        Map<String, Object> configProps = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress
        );
        JsonDeserializer<NullableInteger> keyJsonDeserializer = new JsonDeserializer<>(NullableInteger.class);
        keyJsonDeserializer.addTrustedPackages("*");
        return new DefaultKafkaConsumerFactory<>(configProps, keyJsonDeserializer, new PairDeserializer<>(OptionalEquipment.class, AlgorithmState.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<NullableInteger, Pair<OptionalEquipment, AlgorithmState>> algorithmStatesKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<NullableInteger, Pair<OptionalEquipment, AlgorithmState>> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(algorithmStatesConsumerFactory());
        return factory;
    }

    /**
     * Microservices können eine Analyse-Anfrage empfangen.
     */
    @Bean
    public ConsumerFactory<Integer, AlgorithmResult> receiveAnalysisRequestConsumerFactory(){
        Map<String, Object> configProps = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress
        );
        JsonDeserializer<AlgorithmResult> valueJsonDeserializer = new JsonDeserializer<>(AlgorithmResult.class);
        valueJsonDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(configProps, new IntegerDeserializer(), valueJsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Integer, AlgorithmResult> receiveAnalysisRequestKafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<Integer, AlgorithmResult> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(receiveAnalysisRequestConsumerFactory());
        return factory;
    }

    /**
     * Microservices können das Ergebnis ihrer Analyse an das Frontend senden.
     */
    @Bean
    public ConsumerFactory<Integer, AlgorithmResult> algorithmResultsConsumerFactory() {
        Map<String, Object> configProps = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress
        );
        JsonDeserializer<AlgorithmResult> valueJsonDeserializer = new JsonDeserializer<>(AlgorithmResult.class);
        valueJsonDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(configProps,  new IntegerDeserializer(), valueJsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Integer, AlgorithmResult> algorithmResultsKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, AlgorithmResult> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(algorithmResultsConsumerFactory());
        return factory;
    }

    /**
     * Microservices können vom Engine-Management-Microservice eine Analyse Anfrage erhalten.
     */
    @Bean
    public ConsumerFactory<Integer, String> engMngRequestAnalysisConsumerFactory() {
        Map<String, Object> configProps = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress,
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class
        );
        return new DefaultKafkaConsumerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Integer, String> engMngRequestAnalysisKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(engMngRequestAnalysisConsumerFactory());
        return factory;
    }

    /**
     * Microservices können das Ergebnis ihrer Analyse an den Engine-Management-Microservice senden.
     */
    @Bean
    public ConsumerFactory<Integer, AlgorithmResult> engMngAlgoResultConsumerFactory() {
        Map<String, Object> configProps = Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress
        );
        JsonDeserializer<AlgorithmResult> valueJsonDeserializer = new JsonDeserializer<>(AlgorithmResult.class);
        valueJsonDeserializer.addTrustedPackages("*");

        return new DefaultKafkaConsumerFactory<>(configProps, new IntegerDeserializer(), valueJsonDeserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<Integer, AlgorithmResult> engMngAlgoResultKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, AlgorithmResult> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(engMngAlgoResultConsumerFactory());
        return factory;
    }
}
