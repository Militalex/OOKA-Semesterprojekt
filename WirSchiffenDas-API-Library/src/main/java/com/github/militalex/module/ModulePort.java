package com.github.militalex.module;

import com.github.militalex.messaging.KafkaCircuitBreaker;
import com.github.militalex.messaging.KafkaTopicConfig;
import com.github.militalex.messaging.util.NullableInteger;
import com.github.militalex.messaging.util.Pair;
import com.github.militalex.module.util.AlgorithmResult;
import com.github.militalex.module.util.OptionalEquipment;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

public abstract class ModulePort {

    @Getter
    protected final Modules microservice;
    protected final ModuleService service;
    private final KafkaCircuitBreaker<NullableInteger, Pair<OptionalEquipment, AlgorithmState>> algorithmStatesCB;
    protected final KafkaCircuitBreaker<Integer, AlgorithmResult> algorithmResultToFrontendCB;

    public ModulePort(Modules microservice, ModuleService service,
                      KafkaTemplate<NullableInteger, Pair<OptionalEquipment, AlgorithmState>> stateKafkaTemplate,
                      KafkaTemplate<Integer, AlgorithmResult> resultToFrontendKafkaTemplate
    ) {
        this.microservice = microservice;
        this.service = service;
        service.setPort(this);

        algorithmStatesCB = new KafkaCircuitBreaker<>(pair ->
                stateKafkaTemplate.send(KafkaTopicConfig.FRONTEND_TOPIC, KafkaTopicConfig.FrontendPartitions.ALGORITHM_STATES, pair.getFirst(), pair.getSecond()));
        algorithmResultToFrontendCB = new KafkaCircuitBreaker<>(pair ->
                resultToFrontendKafkaTemplate.send(KafkaTopicConfig.FRONTEND_TOPIC, KafkaTopicConfig.FrontendPartitions.ALGORITHM_RESULTS, pair.getFirst(), pair.getSecond()));
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = KafkaTopicConfig.FRONTEND_TOPIC,
            partitions = "" + KafkaTopicConfig.FrontendPartitions.PING),
            containerFactory = "pingKafkaListenerContainerFactory")
    private void receivePing(@Header(KafkaHeaders.RECEIVED_KEY) int sessionId){
        System.out.println("Received ping from session " + sessionId);
        microservice.getOptionalEquipments().forEach(optionalEquipment ->
                sendAlgorithmState(sessionId, optionalEquipment, AlgorithmState.READY)
        );
    }

    protected abstract void cacheSession(int sessionId, AlgorithmResult emptyAlgoResult);

    @KafkaListener(topicPartitions = @TopicPartition(topic = KafkaTopicConfig.FRONTEND_TOPIC,
            partitions = "" + KafkaTopicConfig.FrontendPartitions.REQUEST_ANALYSIS),
            containerFactory = "receiveAnalysisRequestKafkaListenerContainerFactory")
    private void receiveAnalysisRequestFromFrontend(@Header(KafkaHeaders.RECEIVED_KEY) int sessionId, @Payload AlgorithmResult emptyAlgoResult){
        OptionalEquipment optionalEquipment = emptyAlgoResult.getOptionalEquipment();

        if (microservice.getOptionalEquipments().contains(optionalEquipment)){
            String entry = emptyAlgoResult.getEntry();
            emptyAlgoResult.setEntry(entry); // Ensure entry is set in the cached result.
            cacheSession(sessionId, emptyAlgoResult);

            System.out.println("Received analysis request for " + optionalEquipment + " with entry \"" + entry + "\" from session " + sessionId);
            service.startAnalysis(sessionId, optionalEquipment, entry);
        }
    }

    public void sendAlgorithmState(@Nullable Integer sessionId, OptionalEquipment optionalEquipment, AlgorithmState state) {
        System.out.println("Sending state " + state + " for " + optionalEquipment + " to session " + sessionId);
        algorithmStatesCB.call(new NullableInteger(sessionId), new Pair<>(optionalEquipment, state));
    }

    public void sendAlgorithmResultToFrontend(int sessionId, OptionalEquipment optionalEquipment, String entry, @Nullable Integer result) {
        AlgorithmResult algorithmResult = new AlgorithmResult(sessionId, optionalEquipment, entry, new NullableInteger(result));

        System.out.println("Sending result " + result + " for " + optionalEquipment + " to session " + sessionId);
        algorithmResultToFrontendCB.call(sessionId, algorithmResult);
    }
}
