package com.github.militalex.frontend.analysis;

import com.github.militalex.messaging.KafkaCircuitBreaker;
import com.github.militalex.messaging.KafkaTopicConfig;
import com.github.militalex.messaging.util.NullableInteger;
import com.github.militalex.messaging.util.Pair;
import com.github.militalex.module.AlgorithmState;
import com.github.militalex.module.util.AlgorithmResult;
import com.github.militalex.module.util.OptionalEquipment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class AnalysisPort {

    private final AnalysisService service;
    private final KafkaCircuitBreaker<Integer, String> algorithmPingCB;
    private final KafkaCircuitBreaker<Integer, AlgorithmResult> algorithmRequestAnalysisCB;

    public AnalysisPort(AnalysisService service,
                        @Qualifier("kafkaPingTemplate") KafkaTemplate<Integer, String> pingKafkaTemplate,
                        @Qualifier("kafkaRequestAnalysisTemplate") KafkaTemplate<Integer, AlgorithmResult> requestAnalysisKafkaTemplate) {
        this.service = service;
        service.setPort(this);

        algorithmPingCB = new KafkaCircuitBreaker<>(pair ->
                pingKafkaTemplate.send(KafkaTopicConfig.FRONTEND_TOPIC, KafkaTopicConfig.FrontendPartitions.PING, pair.getFirst(), pair.getSecond()));

        algorithmRequestAnalysisCB = new KafkaCircuitBreaker<>(pair ->
                requestAnalysisKafkaTemplate.send(KafkaTopicConfig.FRONTEND_TOPIC, KafkaTopicConfig.FrontendPartitions.REQUEST_ANALYSIS, pair.getFirst(), pair.getSecond()));
    }

    public void pingAlgorithms(int sessionId) {
        System.out.println("Sending ping to Microservices");
        algorithmPingCB.call(sessionId, "ping");
    }

    public void requestAnalysis(int sessionId, OptionalEquipment optionalEquipment, String entry){
        System.out.println("Requesting analysis for " + optionalEquipment + " from session " + sessionId);
        algorithmRequestAnalysisCB.call(sessionId, new AlgorithmResult(sessionId, optionalEquipment, entry));
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = KafkaTopicConfig.FRONTEND_TOPIC, partitions = "" + KafkaTopicConfig.FrontendPartitions.ALGORITHM_STATES),
            containerFactory = "algorithmStatesKafkaListenerContainerFactory")
    private void receiveAlgorithmState(@Header(KafkaHeaders.RECEIVED_KEY) NullableInteger sessionId,
                                      @Payload Pair<OptionalEquipment, AlgorithmState> pair) {
        OptionalEquipment optionalEquipment = pair.getFirst();
        AlgorithmState algorithmState = pair.getSecond();

        System.out.println("Received algorithm state for " + optionalEquipment + ": " + algorithmState);
        service.setState(sessionId.getValue(), optionalEquipment, algorithmState);
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = KafkaTopicConfig.FRONTEND_TOPIC, partitions = "" + KafkaTopicConfig.FrontendPartitions.ALGORITHM_RESULTS),
            containerFactory = "algorithmResultsKafkaListenerContainerFactory")
    private void receiveAlgorithmResult(@Header(KafkaHeaders.RECEIVED_KEY) int sessionId,
                                      @Payload AlgorithmResult algorithmResult) {
        OptionalEquipment optionalEquipment = algorithmResult.getOptionalEquipment();
        NullableInteger result = algorithmResult.getResult();

        System.out.println("Received algorithm result for " + optionalEquipment + ": " + result);
        service.setResult(sessionId, optionalEquipment, result.getValue());
    }
}
