package com.github.militalex.frontend.analysis.view.components;

import com.github.militalex.frontend.analysis.AnalysisService;
import com.github.militalex.frontend.analysis.entities.AnalysisConfig;
import com.github.militalex.frontend.analysis.entities.AnalysisStates;
import com.github.militalex.microservice.AlgorithmState;
import com.github.militalex.microservice.util.OptionalEquipment;
import com.vaadin.flow.component.HtmlContainer;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.popover.Popover;
import com.vaadin.flow.component.popover.PopoverPosition;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.theme.lumo.LumoUtility;
import kotlin.Pair;

import java.util.Arrays;
import java.util.List;

public final class ComponentFactory {

    public static AnalysisHeader getAnalysisHeader(Binder<AnalysisConfig> configBinder) {
        return new AnalysisHeader(configBinder);
    }
    public static HorizontalLayout getEquipmentRowsHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();

        // "Optional Equipment"
        VerticalLayout optEquipLabelWithinPanel = ComponentFactory.getPanelWithPuttedLabel(new H4("Optional Equipment"),250, FlexComponent.Alignment.CENTER);
        header.setFlexGrow(1.0, optEquipLabelWithinPanel);

        // "Eingaben"
        VerticalLayout inputLabelWithinPanel = ComponentFactory.getPanelWithPuttedLabel(new H4("Eingaben"), 250, FlexComponent.Alignment.START);
        inputLabelWithinPanel.setWidthFull();
        inputLabelWithinPanel.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        header.setFlexGrow(1.0, inputLabelWithinPanel);

        // "Monitoring"
        VerticalLayout monitoringLabelWithinPanel = ComponentFactory.getPanelWithPuttedLabel(new H4("Monitoring"), 200, FlexComponent.Alignment.CENTER);
        header.setFlexGrow(1.0, monitoringLabelWithinPanel);

        // "Results"
        VerticalLayout resultsLabelWithinPanel = ComponentFactory.getPanelWithPuttedLabel(new H4("Einzelanalysen Ergebnisse"), 310, FlexComponent.Alignment.CENTER);
        header.setFlexGrow(1.0, monitoringLabelWithinPanel);

        header.add(optEquipLabelWithinPanel, inputLabelWithinPanel, monitoringLabelWithinPanel, resultsLabelWithinPanel);

        return header;
    }

    public static Hr getHorizontalSeparater(){
        return new Hr();
    }

    public static Hr getHorizontalSeparater(int height_px){
        Hr hr = new Hr();
        hr.setHeight(height_px + "px");
        return hr;
    }

    public static VerticalLayout getPanelWithPuttedLabel(HtmlContainer label, int px_width, FlexComponent.Alignment alignment) {
        if (px_width < 0) throw new IllegalArgumentException("px_width cannot be negative, but it was " + px_width);

        VerticalLayout equipmentPanel = new VerticalLayout();
        equipmentPanel.setPadding(false);
        equipmentPanel.setHeightFull();
        equipmentPanel.setWidth(px_width + "px");
        equipmentPanel.setMinWidth(px_width + "px");
        equipmentPanel.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        equipmentPanel.setAlignSelf(alignment, label);
        equipmentPanel.add(label);
        return equipmentPanel;
    }

    public static Pair<VerticalLayout, ComboBox<String>> getInputsComboBoxWithinPanel(OptionalEquipment optionalEquipment) {
        VerticalLayout comboBoxPanel = new VerticalLayout();
        comboBoxPanel.setHeightFull();
        comboBoxPanel.setSpacing(false);
        comboBoxPanel.setPadding(false);
        comboBoxPanel.getStyle().set("flex-grow", "1");
        comboBoxPanel.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        comboBoxPanel.setAlignItems(FlexComponent.Alignment.START);

        ComboBox<String> comboBox = new ComboBox<>((String) null);
        comboBox.setWidth("100%");
        comboBox.setItems(optionalEquipment.getComboBoxItems());
        comboBox.setValue(optionalEquipment.getComboBoxItems()[0]);
        comboBoxPanel.setAlignSelf(FlexComponent.Alignment.CENTER, comboBox);
        comboBoxPanel.add(comboBox);
        return new Pair<>(comboBoxPanel, comboBox);
    }

    public static VerticalLayout getSingleAnalysisButtonWithinPanel(int sessionId, AnalysisService service, OptionalEquipment optionalEquipment) {
        VerticalLayout buttonPanel = new VerticalLayout();
        buttonPanel.setHeightFull();
        buttonPanel.setPadding(false);
        buttonPanel.addClassName(LumoUtility.Gap.MEDIUM);
        buttonPanel.setWidth("140px");
        buttonPanel.getStyle().set("flex-grow", "1");
        buttonPanel.setAlignItems(FlexComponent.Alignment.CENTER);
        buttonPanel.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        Button button = new Button("Einzelanalyse");
        button.addClickListener(event -> {
            if (service.getState(sessionId, optionalEquipment) == AlgorithmState.OFFLINE){
                Notification notification = new Notification(
                        "Der zuständige Microservice ist derzeit Offline.",
                        5000,
                        Notification.Position.BOTTOM_CENTER
                );
                notification.addThemeName("error");
                notification.open();
            } /*else if (service.getState(sessionId, optionalEquipment) == AlgorithmState.RUNNING) {
                Notification notification = new Notification("Die Analyse läuft bereits!", 5000, Notification.Position.BOTTOM_CENTER);
                notification.addThemeName("warning");
                notification.open();
            }*/ else {
                service.requestAnalysis(sessionId, optionalEquipment);
            }
        });

        buttonPanel.setAlignSelf(FlexComponent.Alignment.CENTER, button);
        buttonPanel.add(button);

        return buttonPanel;
    }

    public static VerticalLayout getMonitoringBadgeWithinPanel(StateBadge stateBadge) {
        VerticalLayout monitoringPanel = new VerticalLayout();
        monitoringPanel.setHeightFull();
        monitoringPanel.setPadding(false);
        monitoringPanel.addClassName(LumoUtility.Gap.MEDIUM);
        monitoringPanel.setWidth("72px");
        monitoringPanel.setMaxWidth("72px");
        monitoringPanel.setMinWidth("72px");
        monitoringPanel.setHeight("100%");
        monitoringPanel.setAlignItems(FlexComponent.Alignment.CENTER);
        monitoringPanel.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        monitoringPanel.add(stateBadge);
        return monitoringPanel;
    }

    public static EquipmentRow getEquipmentRow(int sessionId, AnalysisService service, OptionalEquipment optionalEquipment,
                                               Binder<AnalysisConfig> configBinder, Binder<AnalysisStates> statesBinder) {
        return new EquipmentRow(sessionId, service, optionalEquipment, configBinder, statesBinder);
    }

    public static List<EquipmentRow> getEquipmentRows(int sessionId, AnalysisService service, Binder<AnalysisConfig> configBinder, Binder<AnalysisStates> statesBinder) {
        return List.of(
                ComponentFactory.getEquipmentRow(sessionId, service, OptionalEquipment.STARTING_SYSTEM, configBinder, statesBinder),
                ComponentFactory.getEquipmentRow(sessionId, service, OptionalEquipment.MONITORING_CONTROL_SYSTEM, configBinder, statesBinder),
                ComponentFactory.getEquipmentRow(sessionId, service, OptionalEquipment.ENGINE_MANAGEMENT_SYSTEM, configBinder, statesBinder),
                ComponentFactory.getEquipmentRow(sessionId, service, OptionalEquipment.AUXILIARY_PTO, configBinder, statesBinder),
                ComponentFactory.getEquipmentRow(sessionId, service, OptionalEquipment.MOUNTING_SYSTEM, configBinder, statesBinder),
                ComponentFactory.getEquipmentRow(sessionId, service, OptionalEquipment.GEARBOX_OPTIONS, configBinder, statesBinder),
                ComponentFactory.getEquipmentRow(sessionId, service, OptionalEquipment.OIL_SYSTEM, configBinder, statesBinder),
                ComponentFactory.getEquipmentRow(sessionId, service, OptionalEquipment.FUEL_SYSTEM, configBinder, statesBinder),
                ComponentFactory.getEquipmentRow(sessionId, service, OptionalEquipment.POWER_TRANSMISSION, configBinder, statesBinder),
                ComponentFactory.getEquipmentRow(sessionId, service, OptionalEquipment.COOLING_SYSTEM, configBinder, statesBinder),
                ComponentFactory.getEquipmentRow(sessionId, service, OptionalEquipment.EXHAUST_SYSTEM, configBinder, statesBinder)
        );
    }

    public static Button getAnalysisButton(int sessionId, AnalysisService service){
        Button analyseButton = new Button("Gesamtanalyse", event -> {
            OptionalEquipment[] oes = OptionalEquipment.values();
            if (Arrays.stream(oes).allMatch(optionalEquipment -> service.getState(sessionId, optionalEquipment) == AlgorithmState.OFFLINE)){
                Notification notification = new Notification(
                        "Alle zuständigen Microservices sind derzeit Offline.",
                        5000,
                        Notification.Position.BOTTOM_CENTER
                );
                notification.addThemeName("error");
                notification.open();
                return;
            }

            Arrays.stream(oes)
                    .filter(oe -> List.of(AlgorithmState.READY, AlgorithmState.FINISHED, AlgorithmState.ERROR).contains(service.getState(sessionId, oe)))
                    .forEach(oe -> service.requestAnalysis(sessionId, oe));
        });
        analyseButton.addThemeName("primary");
        return analyseButton;
    }

    public static Button getSaveButton(int sessionId, AnalysisService service){
        return new Button("Speichern", event -> {
            service.saveConfig(sessionId);

            Notification notification = new Notification("Konfiguration gespeichert");
            notification.setPosition(Notification.Position.BOTTOM_CENTER);
            notification.addThemeName("primary");
            notification.setDuration(3000);
            notification.open();
        });
    }

    public static Button getLoadButton(int sessionId, AnalysisService service){

        Button loadButton = new Button("Laden");

        Popover popover = new Popover();
        popover.setModal(true);
        popover.setBackdropVisible(true);
        popover.setPosition(PopoverPosition.START_TOP);
        popover.setTarget(loadButton);

        ComboBox<AnalysisConfig> loadComboBox = new ComboBox<>();
        popover.add(loadComboBox);

        loadButton.addClickListener(event -> {
            AnalysisConfig analysisConfig = service.getAnalysisConfig(sessionId);

            loadComboBox.setItems(service.loadAll().stream().toList());
            loadComboBox.setValue(analysisConfig);
        });

        loadComboBox.addValueChangeListener(event -> {
            AnalysisConfig analysisConfig = service.getAnalysisConfig(sessionId);
            AnalysisConfig selectedConfig = event.getValue();

            if (selectedConfig == null || selectedConfig.equals(analysisConfig)){
                return;
            }
            service.loadConfigByID(sessionId, selectedConfig.getId());
            popover.close();
        });

        return loadButton;
    }

    public static AnalysisFooter getAnalyseFooter(int sessionId, AnalysisService service) {
        return new AnalysisFooter(sessionId, service);
    }
}
