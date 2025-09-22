package com.github.militalex.module.enginemng;

import com.github.militalex.messaging.KafkaCircuitBreaker;
import com.github.militalex.messaging.KafkaTopicConfig;
import com.github.militalex.messaging.util.NullableInteger;
import com.github.militalex.messaging.util.Pair;
import com.github.militalex.module.AlgorithmState;
import com.github.militalex.module.Modules;
import com.github.militalex.module.util.AlgorithmResult;
import com.github.militalex.module.util.OptionalEquipment;
import com.github.militalex.module.util.ResultsPackage;
import com.github.militalex.module.ModulePort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class EngMngModulePort extends ModulePort {

    private final HashMap<Integer, ResultsPackage> sessionResultsMap = new HashMap<>();

    protected final KafkaCircuitBreaker<Integer, String> requestResultCB;

    public EngMngModulePort(EngMngModuleService service,
                            KafkaTemplate<NullableInteger, Pair<OptionalEquipment, AlgorithmState>> stateKafkaTemplate,
                            @Qualifier("kafkaFrontendAlgoResultsTemplate") KafkaTemplate<Integer, AlgorithmResult> resultToFrontendKafkaTemplate,
                            @Qualifier("kafkaEngMngRequestAnalysisTemplate") KafkaTemplate<Integer, String> requestResultKafkaTemplate
    ) {
        super(Modules.ENGINE_MANAGEMENT, service, stateKafkaTemplate, resultToFrontendKafkaTemplate);
        service.setSubPort(this);

        requestResultCB = new KafkaCircuitBreaker<>(pair ->
                requestResultKafkaTemplate.send(KafkaTopicConfig.ENG_MNG_MICROSERVICE_TOPIC, KafkaTopicConfig.EngMngPartitions.REQUEST_RESULT, pair.getFirst(), pair.getSecond()));
    }

    @Override
    protected void cacheSession(int sessionId, AlgorithmResult emptyAlgoResult) {
        // EngineMng does not cache sessions.
    }

    public boolean isComplete(int sessionId){
        return sessionResultsMap.get(sessionId).allResultsPresent();
    }

    public ResultsPackage takeResults(int sessionId){
        return sessionResultsMap.remove(sessionId);
    }

    public void requestResults(int sessionId) {
        System.out.println("Sending request result to EngMng for session " + sessionId);
        sessionResultsMap.put(sessionId, new ResultsPackage());
        requestResultCB.call(sessionId, "RequestResult");
    }

    @KafkaListener(topicPartitions = @TopicPartition(topic = KafkaTopicConfig.ENG_MNG_MICROSERVICE_TOPIC,
            partitions = "" + KafkaTopicConfig.EngMngPartitions.ALGORITHM_RESULTS),
            containerFactory = "engMngAlgoResultKafkaListenerContainerFactory")
    private void receivedAlgorithmResult(@Header(KafkaHeaders.RECEIVED_KEY) int sessionId, @Payload AlgorithmResult algorithmResult){
        System.out.println("Received result " + algorithmResult + " for " + algorithmResult.getOptionalEquipment() + " for session " + sessionId);
        sessionResultsMap.get(sessionId).setResult(algorithmResult);
    }
}
