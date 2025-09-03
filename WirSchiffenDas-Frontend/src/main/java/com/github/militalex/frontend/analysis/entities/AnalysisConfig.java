package com.github.militalex.frontend.analysis.entities;

import com.github.militalex.microservice.util.OptionalEquipment;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
public class AnalysisConfig {

    @Id
    @GeneratedValue
    @Nullable
    private Long id;
    private String name,
            starting_system, auxiliary_pto, oil_system, fuel_system, cooling_system, exhaust_system,
            mounting_system, engine_management_system, monitoring_control_system, power_transmission, gearbox_options;
    @Nullable
    private Integer finalResult,
            res_starting_system, res_auxiliary_pto, res_oil_system, res_fuel_system, res_cooling_system, res_exhaust_system,
            res_mounting_system, res_engine_management_system, res_monitoring_control_system, res_power_transmission, res_gearbox_options;

    public AnalysisConfig(){
        name = "Unbenannte Analyse";
        starting_system = "Air Starter";
        auxiliary_pto = "Alternator";
        oil_system = "Oil replenishment system";
        fuel_system = "Duplex Fuel Pre-filter";
        cooling_system = "Coolant Preheating System";
        exhaust_system = "Wet Exhaust System";
        mounting_system = "Resilient Mounts";
        engine_management_system = "Electronic Control Unit, ECU";
        monitoring_control_system = "Engine Monitoring";
        power_transmission = "Shaft Drive";
        gearbox_options = "Reversing Gearbox";
    }

    public AnalysisConfig(String name, String[] equipment, Integer finalResult, Integer[] single_results) {
        if (equipment.length != 11)
            throw new IllegalArgumentException("equipment.length must be 11, but it was " + equipment.length);
        if (single_results.length != 11)
            throw new IllegalArgumentException("single_results.length must be 11, but it was " + single_results.length);

        this.name = name;
        this.starting_system = equipment[0];
        this.auxiliary_pto = equipment[1];
        this.oil_system = equipment[2];
        this.fuel_system = equipment[3];
        this.cooling_system = equipment[4];
        this.exhaust_system = equipment[5];
        this.mounting_system = equipment[6];
        this.engine_management_system = equipment[7];
        this.monitoring_control_system = equipment[8];
        this.power_transmission = equipment[9];
        this.gearbox_options = equipment[10];

        this.finalResult = finalResult;
        this.res_starting_system = single_results[0];
        this.res_auxiliary_pto = single_results[1];
        this.res_oil_system = single_results[2];
        this.res_fuel_system = single_results[3];
        this.res_cooling_system = single_results[4];
        this.res_exhaust_system = single_results[5];
        this.res_mounting_system = single_results[6];
        this.res_engine_management_system = single_results[7];
        this.res_monitoring_control_system = single_results[8];
        this.res_power_transmission = single_results[9];
        this.res_gearbox_options = single_results[10];
    }

    public void applyFrom(AnalysisConfig analysisConfig){
        this.name = analysisConfig.name;
        this.starting_system = analysisConfig.starting_system;
        this.auxiliary_pto = analysisConfig.auxiliary_pto;
        this.oil_system = analysisConfig.oil_system;
        this.fuel_system = analysisConfig.fuel_system;
        this.cooling_system = analysisConfig.cooling_system;
        this.exhaust_system = analysisConfig.exhaust_system;
        this.mounting_system = analysisConfig.mounting_system;
        this.engine_management_system = analysisConfig.engine_management_system;
        this.monitoring_control_system = analysisConfig.monitoring_control_system;
        this.power_transmission = analysisConfig.power_transmission;
        this.gearbox_options = analysisConfig.gearbox_options;

        this.res_starting_system = analysisConfig.res_starting_system;
        this.res_auxiliary_pto = analysisConfig.res_auxiliary_pto;
        this.res_oil_system = analysisConfig.res_oil_system;
        this.res_fuel_system = analysisConfig.res_fuel_system;
        this.res_cooling_system = analysisConfig.res_cooling_system;
        this.res_exhaust_system = analysisConfig.res_exhaust_system;
        this.res_mounting_system = analysisConfig.res_mounting_system;
        this.res_engine_management_system = analysisConfig.res_engine_management_system;
        this.res_monitoring_control_system = analysisConfig.res_monitoring_control_system;
        this.res_power_transmission = analysisConfig.res_power_transmission;
        this.res_gearbox_options = analysisConfig.res_gearbox_options;
        updateFinalResult();
    }

    public String getEntry(OptionalEquipment optionalEquipment){
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

    public boolean isResultPresent(OptionalEquipment optionalEquipment){
        return getResult(optionalEquipment) != null;
    }

    public Integer getResult(OptionalEquipment optionalEquipment){
        return switch (optionalEquipment){
            case STARTING_SYSTEM -> res_starting_system;
            case AUXILIARY_PTO -> res_auxiliary_pto;
            case OIL_SYSTEM -> res_oil_system;
            case FUEL_SYSTEM -> res_fuel_system;
            case COOLING_SYSTEM -> res_cooling_system;
            case EXHAUST_SYSTEM -> res_exhaust_system;
            case MOUNTING_SYSTEM -> res_mounting_system;
            case ENGINE_MANAGEMENT_SYSTEM -> res_engine_management_system;
            case MONITORING_CONTROL_SYSTEM -> res_monitoring_control_system;
            case POWER_TRANSMISSION -> res_power_transmission;
            case GEARBOX_OPTIONS -> res_gearbox_options;
        };
    }

    public void setResult(OptionalEquipment optionalEquipment, Integer result){
        switch (optionalEquipment){
            case STARTING_SYSTEM -> res_starting_system = result;
            case AUXILIARY_PTO -> res_auxiliary_pto = result;
            case OIL_SYSTEM -> res_oil_system = result;
            case FUEL_SYSTEM -> res_fuel_system = result;
            case COOLING_SYSTEM -> res_cooling_system = result;
            case EXHAUST_SYSTEM -> res_exhaust_system = result;
            case MOUNTING_SYSTEM -> res_mounting_system = result;
            case ENGINE_MANAGEMENT_SYSTEM -> res_engine_management_system = result;
            case MONITORING_CONTROL_SYSTEM -> res_monitoring_control_system = result;
            case POWER_TRANSMISSION -> res_power_transmission = result;
            case GEARBOX_OPTIONS -> res_gearbox_options = result;
        }
        updateFinalResult();
    }

    private void updateFinalResult(){
        List<Integer> results = getResultsAsList();

        Integer sum = null;

        for (Integer result : results) {
            if (result == null) continue;
            if (sum == null) {
                sum = result;
            } else {
                sum += result;
            }
        }
        finalResult = (sum == null) ? null : (sum / results.size());
    }

    @NotNull
    private List<Integer> getResultsAsList() {
        List<Integer> results = new ArrayList<>();
        results.add(res_starting_system);
        results.add(res_auxiliary_pto);
        results.add(res_oil_system);
        results.add(res_fuel_system);
        results.add(res_cooling_system);
        results.add(res_exhaust_system);
        results.add(res_mounting_system);
        results.add(res_engine_management_system);
        results.add(res_monitoring_control_system);
        results.add(res_power_transmission);
        results.add(res_gearbox_options);
        return results;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnalysisConfig config)) return false;

        if (!id.equals(config.id)) return false;
        if (!name.equals(config.name)) return false;
        if (!starting_system.equals(config.starting_system)) return false;
        if (!auxiliary_pto.equals(config.auxiliary_pto)) return false;
        if (!oil_system.equals(config.oil_system)) return false;
        if (!fuel_system.equals(config.fuel_system)) return false;
        if (!cooling_system.equals(config.cooling_system)) return false;
        if (!exhaust_system.equals(config.exhaust_system)) return false;
        if (!mounting_system.equals(config.mounting_system)) return false;
        if (!engine_management_system.equals(config.engine_management_system)) return false;
        if (!monitoring_control_system.equals(config.monitoring_control_system)) return false;
        if (!power_transmission.equals(config.power_transmission)) return false;
        if (!gearbox_options.equals(config.gearbox_options)) return false;
        if (!Objects.equals(finalResult, config.finalResult)) return false;
        if (!Objects.equals(res_starting_system, config.res_starting_system))
            return false;
        if (!Objects.equals(res_auxiliary_pto, config.res_auxiliary_pto))
            return false;
        if (!Objects.equals(res_oil_system, config.res_oil_system))
            return false;
        if (!Objects.equals(res_fuel_system, config.res_fuel_system))
            return false;
        if (!Objects.equals(res_cooling_system, config.res_cooling_system))
            return false;
        if (!Objects.equals(res_exhaust_system, config.res_exhaust_system))
            return false;
        if (!Objects.equals(res_mounting_system, config.res_mounting_system))
            return false;
        if (!Objects.equals(res_engine_management_system, config.res_engine_management_system))
            return false;
        if (!Objects.equals(res_monitoring_control_system, config.res_monitoring_control_system))
            return false;
        if (!Objects.equals(res_power_transmission, config.res_power_transmission))
            return false;
        return Objects.equals(res_gearbox_options, config.res_gearbox_options);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + starting_system.hashCode();
        result = 31 * result + auxiliary_pto.hashCode();
        result = 31 * result + oil_system.hashCode();
        result = 31 * result + fuel_system.hashCode();
        result = 31 * result + cooling_system.hashCode();
        result = 31 * result + exhaust_system.hashCode();
        result = 31 * result + mounting_system.hashCode();
        result = 31 * result + engine_management_system.hashCode();
        result = 31 * result + monitoring_control_system.hashCode();
        result = 31 * result + power_transmission.hashCode();
        result = 31 * result + gearbox_options.hashCode();
        result = 31 * result + (finalResult != null ? finalResult.hashCode() : 0);
        result = 31 * result + (res_starting_system != null ? res_starting_system.hashCode() : 0);
        result = 31 * result + (res_auxiliary_pto != null ? res_auxiliary_pto.hashCode() : 0);
        result = 31 * result + (res_oil_system != null ? res_oil_system.hashCode() : 0);
        result = 31 * result + (res_fuel_system != null ? res_fuel_system.hashCode() : 0);
        result = 31 * result + (res_cooling_system != null ? res_cooling_system.hashCode() : 0);
        result = 31 * result + (res_exhaust_system != null ? res_exhaust_system.hashCode() : 0);
        result = 31 * result + (res_mounting_system != null ? res_mounting_system.hashCode() : 0);
        result = 31 * result + (res_engine_management_system != null ? res_engine_management_system.hashCode() : 0);
        result = 31 * result + (res_monitoring_control_system != null ? res_monitoring_control_system.hashCode() : 0);
        result = 31 * result + (res_power_transmission != null ? res_power_transmission.hashCode() : 0);
        result = 31 * result + (res_gearbox_options != null ? res_gearbox_options.hashCode() : 0);
        return result;
    }
}
