package com.github.militalex.frontend.analysis.view.components;

import com.github.militalex.frontend.analysis.AnalysisService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public final class AnalysisFooter extends HorizontalLayout {
    AnalysisFooter(int sessionId, AnalysisService service) {
        setWidthFull();
        setJustifyContentMode(JustifyContentMode.END);
        setHeight("min-content");

        Button analyseButton = ComponentFactory.getAnalysisButton(sessionId, service);
        Button saveButton = ComponentFactory.getSaveButton(sessionId, service);
        Button loadButton = ComponentFactory.getLoadButton(sessionId, service);

        setAlignSelf(Alignment.END, analyseButton, saveButton, loadButton);
        add(analyseButton, saveButton, loadButton);
    }
}
