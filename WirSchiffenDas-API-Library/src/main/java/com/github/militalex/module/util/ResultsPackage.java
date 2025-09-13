package com.github.militalex.module.util;

import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

@NoArgsConstructor
public class ResultsPackage {
    @Nullable
    private AlgorithmResult res_starting_system, res_auxiliary_pto, res_oil_system, res_fuel_system, res_cooling_system, res_exhaust_system,
            res_mounting_system, res_monitoring_control_system, res_power_transmission, res_gearbox_options;

    public boolean allResultsPresent(){
        return res_starting_system != null && res_auxiliary_pto != null && res_oil_system != null
                && res_fuel_system != null && res_cooling_system != null && res_exhaust_system != null
                && res_mounting_system != null && res_monitoring_control_system != null
                && res_power_transmission != null && res_gearbox_options != null;
    }

    public void setResult(AlgorithmResult algorithmResult){
        switch (algorithmResult.getOptionalEquipment()){
            case STARTING_SYSTEM -> this.res_starting_system = algorithmResult;
            case AUXILIARY_PTO -> this.res_auxiliary_pto = algorithmResult;
            case OIL_SYSTEM -> this.res_oil_system = algorithmResult;
            case FUEL_SYSTEM -> this.res_fuel_system = algorithmResult;
            case COOLING_SYSTEM -> this.res_cooling_system = algorithmResult;
            case EXHAUST_SYSTEM -> this.res_exhaust_system = algorithmResult;
            case MOUNTING_SYSTEM -> this.res_mounting_system = algorithmResult;
            case MONITORING_CONTROL_SYSTEM -> this.res_monitoring_control_system = algorithmResult;
            case POWER_TRANSMISSION -> this.res_power_transmission = algorithmResult;
            case GEARBOX_OPTIONS -> this.res_gearbox_options = algorithmResult;
            case ENGINE_MANAGEMENT_SYSTEM -> throw new IllegalArgumentException("Engine Management System result is not part of ResultsPackage.");
        }
    }

    public AlgorithmResult getResult(OptionalEquipment optionalEquipment){
        return switch (optionalEquipment){
            case STARTING_SYSTEM -> this.res_starting_system;
            case AUXILIARY_PTO -> this.res_auxiliary_pto;
            case OIL_SYSTEM -> this.res_oil_system;
            case FUEL_SYSTEM -> this.res_fuel_system;
            case COOLING_SYSTEM -> this.res_cooling_system;
            case EXHAUST_SYSTEM -> this.res_exhaust_system;
            case MOUNTING_SYSTEM -> this.res_mounting_system;
            case MONITORING_CONTROL_SYSTEM -> this.res_monitoring_control_system;
            case POWER_TRANSMISSION -> this.res_power_transmission;
            case GEARBOX_OPTIONS -> this.res_gearbox_options;
            case ENGINE_MANAGEMENT_SYSTEM -> throw new IllegalArgumentException("Engine Management System result is not part of ResultsPackage.");
        };
    }
}
