package com.github.militalex.frontend.analysis.view.components;

import com.github.militalex.frontend.analysis.AnalysisService;
import com.github.militalex.frontend.analysis.entities.AnalysisConfig;
import com.github.militalex.frontend.analysis.entities.AnalysisStates;
import com.github.militalex.microservice.util.OptionalEquipment;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import kotlin.Pair;
import lombok.Getter;
import lombok.Setter;

public final class EquipmentRow extends HorizontalLayout {
    private final StateBadge stateBadge;
    private final ResultComp resultComponent;
    private final ComboBox<String> comboBox;

    @Getter @Setter
    private boolean resultShown = false;

    EquipmentRow(int sessionId, AnalysisService service, OptionalEquipment optionalEquipment, Binder<AnalysisConfig> configBinder, Binder<AnalysisStates> statesBinder) {
        setPadding(false);
        setWidthFull();
        setWidth("100%");
        setHeight("min-content");
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        resultComponent = new ResultComp(false);
        stateBadge = new StateBadge();

        // Equipment Label
        VerticalLayout equipmentPanel = ComponentFactory.getPanelWithPuttedLabel(new H4(optionalEquipment.getEquipmentName()), 250, Alignment.CENTER);
        setFlexGrow(1, equipmentPanel);

        // ComboBox
        Pair<VerticalLayout, ComboBox<String>> comboBoxPair = ComponentFactory.getInputsComboBoxWithinPanel(optionalEquipment);
        VerticalLayout comboBoxPanel = comboBoxPair.getFirst();
        comboBox = comboBoxPair.getSecond();
        setFlexGrow(1, comboBoxPanel);

        // Button
        VerticalLayout buttonPanel = ComponentFactory.getSingleAnalysisButtonWithinPanel(sessionId, service, optionalEquipment);
        setFlexGrow(1, buttonPanel);

        // Monitoring
        VerticalLayout monitoringPanel = ComponentFactory.getMonitoringBadgeWithinPanel(stateBadge);
        setFlexGrow(1.0, monitoringPanel);

        add(equipmentPanel, comboBoxPanel, buttonPanel, monitoringPanel, resultComponent);

        // Binding
        configBinder.bind(comboBox, optionalEquipment.id);
        configBinder.bind(resultComponent, "res_" + optionalEquipment.id);
        statesBinder.bind(stateBadge, optionalEquipment.id);
    }
}
