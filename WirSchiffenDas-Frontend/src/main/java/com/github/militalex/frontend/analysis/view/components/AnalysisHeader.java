package com.github.militalex.frontend.analysis.view.components;

import com.github.militalex.frontend.analysis.entities.AnalysisConfig;
import com.github.militalex.frontend.analysis.view.AnalysisView;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

public final class AnalysisHeader extends HorizontalLayout {

    AnalysisHeader(Binder<AnalysisConfig> binder) {
        ResultComp finalResultComponent = new ResultComp(true);
        TextField analysisNameField = new TextField(null, "Unbenannte Analyse", "");

        binder.bind(analysisNameField, "name");
        binder.bind(finalResultComponent, "finalResult");

        setWidth("100%");
        setHeight("min-content");
        setAlignItems(FlexComponent.Alignment.CENTER);
        add(new H2("Analyse: "),
                analysisNameField,
                new H2(" -- "),
                new H2("Gesamtergebnis: "),
                finalResultComponent
        );
    }
}
