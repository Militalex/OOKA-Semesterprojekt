package com.github.militalex.frontend.analysis.view.components;

import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;

public final class ResultComp extends AbstractCompositeField<HorizontalLayout, ResultComp, Integer> {

    private final ResultBadge resultBadge = new ResultBadge();
    private final ResultText resultText;

    ResultComp(boolean big) {
        super(null);
        resultText = new ResultText(big);

        getContent().setHeightFull();
        getContent().setPadding(false);
        getContent().addClassName(LumoUtility.Gap.MEDIUM);
        getContent().setWidth(big ? "370px" : "310px");
        getContent().setMinWidth(big ? "370px" : "310px");
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, resultBadge);
        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, resultText);
        getContent().add(resultBadge, resultText);
    }

    @Override
    protected void setPresentationValue(Integer result_percentage) {
        resultText.showResult(result_percentage);
        resultBadge.setState(resultText.getState());
    }
}
