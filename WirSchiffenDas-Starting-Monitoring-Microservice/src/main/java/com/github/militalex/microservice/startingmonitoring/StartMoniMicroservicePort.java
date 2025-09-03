package com.github.militalex.microservice.startingmonitoring;

import com.github.militalex.messaging.util.NullableInteger;
import com.github.militalex.messaging.util.Pair;
import com.github.militalex.microservice.AlgorithmState;
import com.github.militalex.microservice.Microservice;
import com.github.militalex.microservice.MicroservicePortWithCache;
import com.github.militalex.microservice.util.AlgorithmResult;
import com.github.militalex.microservice.util.OptionalEquipment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class StartMoniMicroservicePort extends MicroservicePortWithCache {
    public StartMoniMicroservicePort(StartMoniMicroserviceService service,
                                     KafkaTemplate<NullableInteger, Pair<OptionalEquipment, AlgorithmState>> stateKafkaTemplate,
                                     @Qualifier("kafkaFrontendAlgoResultsTemplate") KafkaTemplate<Integer, AlgorithmResult> resultToFrontendKafkaTemplate,
                                     @Qualifier("kafkaEngMngAlgoResultsTemplate") KafkaTemplate<Integer, AlgorithmResult> resultToEngMngKafkaTemplate
    ) {
        super(Microservice.STARTING_MONITORING_MICROSERVICE, service, stateKafkaTemplate, resultToFrontendKafkaTemplate, resultToEngMngKafkaTemplate);
    }
}
