package com.github.militalex.microservice;

import com.github.militalex.microservice.util.OptionalEquipment;
import lombok.Getter;

import java.util.List;

@Getter
public enum Microservice {
    STARTING_MONITORING_MICROSERVICE(List.of(
            OptionalEquipment.STARTING_SYSTEM,
            OptionalEquipment.MONITORING_CONTROL_SYSTEM
    )),
    ENGINE_MANAGEMENT_MICROSERVICE(List.of(
            OptionalEquipment.ENGINE_MANAGEMENT_SYSTEM
    )),
    MOUNTING_MICROSERVICE(List.of(
            OptionalEquipment.AUXILIARY_PTO,
            OptionalEquipment.MOUNTING_SYSTEM,
            OptionalEquipment.GEARBOX_OPTIONS
    )),
    FUEL_MANAGEMENT_MICROSERVICE(List.of(
            OptionalEquipment.OIL_SYSTEM,
            OptionalEquipment.FUEL_SYSTEM,
            OptionalEquipment.POWER_TRANSMISSION
    )),
    COOLING_MANAGEMENT_MICROSERVICE(List.of(
            OptionalEquipment.COOLING_SYSTEM,
            OptionalEquipment.EXHAUST_SYSTEM
    ));

    private final List<OptionalEquipment> optionalEquipments;

    Microservice(List<OptionalEquipment> optionalEquipments) {
        this.optionalEquipments = optionalEquipments;
    }
}
