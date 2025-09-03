package com.github.militalex.microservice.util;

import lombok.Getter;

@Getter
public enum OptionalEquipment {
    STARTING_SYSTEM("Starting System", "Air Starter", "Electric Starter", "Hydraulic Starter", "Manual Crank", "Hand Starter"),
    AUXILIARY_PTO("Auxiliary PTO", "Alternator", "Watermaker", "Hydraulic Pump", "Steering Pump", "Air Conditioning/Refrigeration", "Generator", "Fire Pump"),
    OIL_SYSTEM("Oil System", "Oil replenishment system", "Oil Extraction System", "Oil Cooling System", "Oil Filtration System", "Oil Separator System"),
    FUEL_SYSTEM("Fuel System", "Duplex Fuel Pre-filter", "Simplex Fuel Pre-filter", "Main Fuel Filter", "Electric Fuel Lift Pump", "Mechanical Fuel Lift Pump"),
    COOLING_SYSTEM("Cooling System", "Coolant Preheating System", "Direct Cooling System", "Indirect Cooling System", "Coolant Pump", "Seawater Pump", "Heat Exchanger"),
    EXHAUST_SYSTEM("Exhaust System", "Wet Exhaust System", "Dry Exhaust System", "Exhaust Water Temperature Sensor", "Exhaust Gas Detector", "Exhaust Check Valve", "Exhaust Hose/Flex Pipe", "Exhaust Manifold"),
    MOUNTING_SYSTEM("Mounting System", "Resilient Mounts", "Engine Mounts", "Rigid Mounts", "Pump Mounts", "Gearbox Mounts", "Generator Mounting System", "Dampening Plates"),
    ENGINE_MANAGEMENT_SYSTEM("Engine Management System", "Electronic Control Unit, ECU", "Diagnostic System", "Fuel Injection System", "Throttle Control System"),
    MONITORING_CONTROL_SYSTEM("Monitoring/Control System", "Engine Monitoring", "Tank Monitoring", "Navigation System", "Autopilot System", "Electrical Monitoring"),
    POWER_TRANSMISSION("Power Transmission", "Shaft Drive", "Sterndrive", "Pod Drive", "Waterjet Propulsion", "Sail Drive"),
    GEARBOX_OPTIONS("Gearbox Options", "Reversing Gearbox", "Reduction Gearbox", "Two-Speed Gearbox", "Hydraulic Gearbox", "Electronic Gearbox");

    public final String id = name().toLowerCase();
    public final String equipmentName;
    public final String[] comboBoxItems;

    OptionalEquipment(String equipmentName, String... comboBoxItems) {
        this.equipmentName = equipmentName;
        this.comboBoxItems = comboBoxItems;
    }
}
