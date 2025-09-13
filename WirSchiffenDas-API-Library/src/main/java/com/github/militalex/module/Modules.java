package com.github.militalex.module;

import com.github.militalex.module.util.OptionalEquipment;
import lombok.Getter;

import java.util.List;

@Getter
public enum Modules {
    STARTING_MONITORING(List.of(
            OptionalEquipment.STARTING_SYSTEM,
            OptionalEquipment.MONITORING_CONTROL_SYSTEM
    )),
    ENGINE_MANAGEMENT(List.of(
            OptionalEquipment.ENGINE_MANAGEMENT_SYSTEM
    )),
    MOUNTING(List.of(
            OptionalEquipment.AUXILIARY_PTO,
            OptionalEquipment.MOUNTING_SYSTEM,
            OptionalEquipment.GEARBOX_OPTIONS
    )),
    FUEL_MANAGEMENT(List.of(
            OptionalEquipment.OIL_SYSTEM,
            OptionalEquipment.FUEL_SYSTEM,
            OptionalEquipment.POWER_TRANSMISSION
    )),
    COOLING_MANAGEMENT(List.of(
            OptionalEquipment.COOLING_SYSTEM,
            OptionalEquipment.EXHAUST_SYSTEM
    ));

    private final List<OptionalEquipment> optionalEquipments;

    Modules(List<OptionalEquipment> optionalEquipments) {
        this.optionalEquipments = optionalEquipments;
    }
}
