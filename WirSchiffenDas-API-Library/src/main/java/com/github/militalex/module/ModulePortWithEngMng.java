package com.github.militalex.module;

import com.github.militalex.messaging.KafkaCircuitBreaker;
import com.github.militalex.messaging.KafkaTopicConfig;
import com.github.militalex.messaging.util.NullableInteger;
import com.github.militalex.messaging.util.Pair;
import com.github.militalex.module.util.AlgorithmResult;
import com.github.militalex.module.util.IResultCaching;
import com.github.militalex.module.util.ListResultCache;
import com.github.militalex.module.util.OptionalEquipment;
import org.apache.kafka.common.errors.TimeoutException;
import org.jetbrains.annotations.Nullable;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.util.MultiValueMapAdapter;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public abstract class ModulePortWithEngMng extends ModulePort {

    private final MultiValueMapAdapter<Integer, OptionalEquipment> cachedSessionsFromFrontend = new MultiValueMapAdapter<>(new HashMap<>());
    private final IResultCaching resultCache = new ListResultCache();
    private final KafkaCircuitBreaker<Integer, AlgorithmResult> algorithmResultToEngMngCB;
    public ModulePortWithEngMng(Modules microservice, ModuleService service,
                                KafkaTemplate<NullableInteger, Pair<OptionalEquipment, AlgorithmState>> stateKafkaTemplate,
                                KafkaTemplate<Integer, AlgorithmResult> resultToFrontendKafkaTemplate,
                                KafkaTemplate<Integer, AlgorithmResult> resultToEngMngKafkaTemplate) {
        super(microservice, service, stateKafkaTemplate, resultToFrontendKafkaTemplate);
        algorithmResultToEngMngCB = new KafkaCircuitBreaker<>(pair ->
                resultToEngMngKafkaTemplate.send(KafkaTopicConfig.ENG_MNG_MICROSERVICE_TOPIC, KafkaTopicConfig.EngMngPartitions.ALGORITHM_RESULTS, pair.getFirst(), pair.getSecond()));
    }

    @Override
    protected void cacheSession(int sessionId, AlgorithmResult emptyAlgoResult) {
        if (resultCache.isPresent(sessionId, emptyAlgoResult.getOptionalEquipment())){
            resultCache.takeResult(sessionId, emptyAlgoResult.getOptionalEquipment());
        }
        resultCache.cacheSession(sessionId, emptyAlgoResult);
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = KafkaTopicConfig.FRONTEND_TOPIC,
            partitions = "" + KafkaTopicConfig.FrontendPartitions.REQUEST_ANALYSIS),
            containerFactory = "receiveAnalysisRequestKafkaListenerContainerFactory")
    private void receiveAnalysisRequestFromFrontend(@Header(KafkaHeaders.RECEIVED_KEY) int sessionId, @Payload AlgorithmResult emptyAlgoResult){
        OptionalEquipment optionalEquipment = emptyAlgoResult.getOptionalEquipment();

        if (module.getOptionalEquipments().contains(optionalEquipment)
                && !Optional.ofNullable(cachedSessionsFromFrontend.get(sessionId)).orElse(Collections.emptyList()).contains(optionalEquipment)){
            receivedAnalysisRequest(sessionId, emptyAlgoResult, true);
        }
    }

    protected void receivedAnalysisRequest(int sessionId, AlgorithmResult emptyAlgoResult, boolean frontend){
        OptionalEquipment optionalEquipment = emptyAlgoResult.getOptionalEquipment();

        if (frontend) {
            cachedSessionsFromFrontend.add(sessionId, optionalEquipment);
        }

        String entry = emptyAlgoResult.getEntry();
        emptyAlgoResult.setEntry(entry); // Ensure entry is set in the cached result.
        cacheSession(sessionId, emptyAlgoResult);

        System.out.println("Received analysis request for " + optionalEquipment + " with entry \"" + entry + "\" from session " + sessionId);
        service.startAnalysis(sessionId, optionalEquipment, entry);
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = KafkaTopicConfig.ENG_MNG_MICROSERVICE_TOPIC,
            partitions = "" + KafkaTopicConfig.EngMngPartitions.REQUEST_RESULT),
            containerFactory = "engMngRequestAnalysisKafkaListenerContainerFactory")
    private void receiveAnalysisRequestFromEngMng(@Header(KafkaHeaders.RECEIVED_KEY) int sessionId){
        module.getOptionalEquipments().stream()
                .filter(oe -> !resultCache.isPresent(oe))
                .forEach(oe -> receivedAnalysisRequest(sessionId, new AlgorithmResult(sessionId, oe, "Not yet set"), false)
        );

        CompletableFuture<Void> futureResult = CompletableFuture.runAsync(() -> {
            int maxSeconds = 30;
            for (int i = 0; i < maxSeconds; i++){
                if (module.getOptionalEquipments().stream()
                        .allMatch(optionalEquipment -> resultCache.isResultPresent(sessionId, optionalEquipment))){
                    module.getOptionalEquipments().forEach(optionalEquipment -> {
                        AlgorithmResult algorithmResult = resultCache.takeResult(sessionId, optionalEquipment);
                        System.out.println("Sending result " + algorithmResult + " for " + optionalEquipment + " to EngMng for session " + sessionId);
                        algorithmResultToEngMngCB.call(sessionId, algorithmResult);
                    });
                    return;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Warten auf Ergebnisse wurde unterbrochen: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
            throw new TimeoutException("Timeout: Not all results were ready after " + maxSeconds + " seconds.");
        });
        futureResult.whenComplete((unused, throwable) -> {
            if (throwable != null) {
                System.err.println("Error while waiting for all results to be ready for session " + sessionId + ": " + throwable.getMessage());
                module.getOptionalEquipments().forEach(optionalEquipment -> {
                    System.out.println("Sending result " + null + " for " + optionalEquipment + " to EngMng for session " + sessionId);
                    algorithmResultToEngMngCB.call(sessionId, new AlgorithmResult(sessionId, optionalEquipment, "Error"));
                });
            }
        });
    }

    @Override
    public void sendAlgorithmResultToFrontend(int sessionId, OptionalEquipment optionalEquipment, String entry, @Nullable Integer result) {
        AlgorithmResult algorithmResult = resultCache.cacheResult(sessionId, optionalEquipment, result);

        if (cachedSessionsFromFrontend.get(sessionId).contains(optionalEquipment)){
            System.out.println("Sending result " + result + " for " + optionalEquipment + " to session " + sessionId);
            algorithmResultToFrontendCB.call(sessionId, algorithmResult);
            delete(sessionId, optionalEquipment);
        }
    }

    private void delete(Integer key, OptionalEquipment value){
        if (!cachedSessionsFromFrontend.containsKey(key))
            return;
        cachedSessionsFromFrontend.get(key).remove(value);
        if (cachedSessionsFromFrontend.get(key).isEmpty())
            cachedSessionsFromFrontend.remove(key);
    }
}
