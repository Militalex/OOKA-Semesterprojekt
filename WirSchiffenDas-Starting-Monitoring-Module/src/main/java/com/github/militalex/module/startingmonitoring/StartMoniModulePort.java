package com.github.militalex.module.startingmonitoring;

import com.github.militalex.messaging.util.NullableInteger;
import com.github.militalex.messaging.util.Pair;
import com.github.militalex.module.AlgorithmState;
import com.github.militalex.module.Modules;
import com.github.militalex.module.util.AlgorithmResult;
import com.github.militalex.module.util.OptionalEquipment;
import com.github.militalex.module.ModulePortWithEngMng;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class StartMoniModulePort extends ModulePortWithEngMng {
    public StartMoniModulePort(StartMoniModuleService service,
                               KafkaTemplate<NullableInteger, Pair<OptionalEquipment, AlgorithmState>> stateKafkaTemplate,
                               @Qualifier("kafkaFrontendAlgoResultsTemplate") KafkaTemplate<Integer, AlgorithmResult> resultToFrontendKafkaTemplate,
                               @Qualifier("kafkaEngMngAlgoResultsTemplate") KafkaTemplate<Integer, AlgorithmResult> resultToEngMngKafkaTemplate
    ) {
        super(Modules.STARTING_MONITORING, service, stateKafkaTemplate, resultToFrontendKafkaTemplate, resultToEngMngKafkaTemplate);
    }
}
