package com.github.militalex.microservice.fuel;

import com.github.militalex.messaging.util.NullableInteger;
import com.github.militalex.messaging.util.Pair;
import com.github.militalex.microservice.*;
import com.github.militalex.microservice.util.AlgorithmResult;
import com.github.militalex.microservice.util.OptionalEquipment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class FuelMicroservicePort extends MicroservicePortWithCache {
    public FuelMicroservicePort(FuelMicroserviceService service, KafkaTemplate<NullableInteger, Pair<OptionalEquipment, AlgorithmState>> stateKafkaTemplate,
                                @Qualifier("kafkaFrontendAlgoResultsTemplate") KafkaTemplate<Integer, AlgorithmResult> resultToFrontendKafkaTemplate,
                                @Qualifier("kafkaEngMngAlgoResultsTemplate") KafkaTemplate<Integer, AlgorithmResult> resultToEngMngKafkaTemplate
    ) {
        super(Microservice.FUEL_MANAGEMENT_MICROSERVICE, service, stateKafkaTemplate, resultToFrontendKafkaTemplate, resultToEngMngKafkaTemplate);
    }
}
