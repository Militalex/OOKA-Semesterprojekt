package com.github.militalex.frontend.analysis.entities;

import com.github.militalex.microservice.AlgorithmState;
import com.github.militalex.microservice.util.OptionalEquipment;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
public class AnalysisStates {
    @Id
    @GeneratedValue
    private Long id;

    private AlgorithmState starting_system, auxiliary_pto, oil_system, fuel_system, cooling_system, exhaust_system,
            mounting_system, engine_management_system, monitoring_control_system, power_transmission, gearbox_options;

    public AnalysisStates(){
        starting_system = AlgorithmState.OFFLINE;
        auxiliary_pto = AlgorithmState.OFFLINE;
        oil_system = AlgorithmState.OFFLINE;
        fuel_system = AlgorithmState.OFFLINE;
        cooling_system = AlgorithmState.OFFLINE;
        exhaust_system = AlgorithmState.OFFLINE;
        mounting_system = AlgorithmState.OFFLINE;
        engine_management_system = AlgorithmState.OFFLINE;
        monitoring_control_system = AlgorithmState.OFFLINE;
        power_transmission = AlgorithmState.OFFLINE;
        gearbox_options = AlgorithmState.OFFLINE;
    }
    
    public AlgorithmState getState(OptionalEquipment optionalEquipment){
        return switch (optionalEquipment){
            case STARTING_SYSTEM -> starting_system;
            case AUXILIARY_PTO -> auxiliary_pto;
            case OIL_SYSTEM -> oil_system;
            case FUEL_SYSTEM -> fuel_system;
            case COOLING_SYSTEM -> cooling_system;
            case EXHAUST_SYSTEM -> exhaust_system;
            case MOUNTING_SYSTEM -> mounting_system;
            case ENGINE_MANAGEMENT_SYSTEM -> engine_management_system;
            case MONITORING_CONTROL_SYSTEM -> monitoring_control_system;
            case POWER_TRANSMISSION -> power_transmission;
            case GEARBOX_OPTIONS -> gearbox_options;
        };
    }

    public void setState(OptionalEquipment optionalEquipment, AlgorithmState algorithmState){
        switch (optionalEquipment){
            case STARTING_SYSTEM -> starting_system = algorithmState;
            case AUXILIARY_PTO -> auxiliary_pto = algorithmState;
            case OIL_SYSTEM -> oil_system = algorithmState;
            case FUEL_SYSTEM -> fuel_system = algorithmState;
            case COOLING_SYSTEM -> cooling_system = algorithmState;
            case EXHAUST_SYSTEM -> exhaust_system = algorithmState;
            case MOUNTING_SYSTEM -> mounting_system = algorithmState;
            case ENGINE_MANAGEMENT_SYSTEM -> engine_management_system = algorithmState;
            case MONITORING_CONTROL_SYSTEM -> monitoring_control_system = algorithmState;
            case POWER_TRANSMISSION -> power_transmission = algorithmState;
            case GEARBOX_OPTIONS -> gearbox_options = algorithmState;
        }
    }
}
