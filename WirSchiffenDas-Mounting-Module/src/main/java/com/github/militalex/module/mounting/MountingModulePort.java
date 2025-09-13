package com.github.militalex.module.mounting;

import com.github.militalex.messaging.util.NullableInteger;
import com.github.militalex.messaging.util.Pair;
import com.github.militalex.module.AlgorithmState;
import com.github.militalex.module.ModulePortWithEngMng;
import com.github.militalex.module.Modules;
import com.github.militalex.module.util.AlgorithmResult;
import com.github.militalex.module.util.OptionalEquipment;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MountingModulePort extends ModulePortWithEngMng {
    public MountingModulePort(MountingModuleService service,
                              KafkaTemplate<NullableInteger, Pair<OptionalEquipment, AlgorithmState>> stateKafkaTemplate,
                              @Qualifier("kafkaFrontendAlgoResultsTemplate") KafkaTemplate<Integer, AlgorithmResult> resultToFrontendKafkaTemplate,
                              @Qualifier("kafkaEngMngAlgoResultsTemplate") KafkaTemplate<Integer, AlgorithmResult> resultToEngMngKafkaTemplate
    ) {
        super(Modules.MOUNTING, service, stateKafkaTemplate, resultToFrontendKafkaTemplate, resultToEngMngKafkaTemplate);
    }
}
