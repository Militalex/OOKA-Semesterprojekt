package com.github.militalex.frontend.analysis.view.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import lombok.Getter;

public final class ResultText extends VerticalLayout {

    private static final String DEFAULT_TEXT = "- Nicht vorhanden -";

    private final Component textComponent;
    private final boolean big;
    @Getter
    private ResultState state = ResultState.NO_RESULT;

    ResultText(boolean big) {
        this.big = big;
        this.textComponent = (big) ? new H2(DEFAULT_TEXT) : new H4(DEFAULT_TEXT);

        setPadding(false);
        setHeight("max-content");
        setWidth("max-content");
        setFlexGrow(1.0, textComponent);
        add(textComponent);
    }

    void showResult(Integer result_percentage) {
        if (result_percentage == null) {
            removeResult();
            return;
        }

        if (result_percentage < 0 || result_percentage > 100){
            throw new IllegalArgumentException("Percentage must be between 0 and 100");
        }
        state = ResultState.FAILED;
        if (result_percentage >= 75) {
            state = ResultState.PASSED;
            addClassNameToText("green_text");
        }
        else if (result_percentage >= 40) {
            state = ResultState.PARTIAL_PASSED;
            addClassNameToText("orange_text");
        }

        if (state == ResultState.FAILED) {
            addClassNameToText("red_text");
        }

        if (big){
            ((H2)textComponent).setText(state.getText() + " with " + result_percentage + "%");
        }
        else {
            ((H4)textComponent).setText(state.getText() + " with " + result_percentage + "%");
        }

    }

    private void clearClassNamesFromText() {
        textComponent.removeClassName("green_text");
        textComponent.removeClassName("orange_text");
        textComponent.removeClassName("red_text");
    }

    public void addClassNameToText(String className) {
        clearClassNamesFromText();
        textComponent.addClassName(className);
    }

    private void removeResult() {
        if (big){
            ((H2)textComponent).setText(DEFAULT_TEXT);
        }
        else {
            ((H4)textComponent).setText(DEFAULT_TEXT);
        }
        clearClassNamesFromText();
        state = ResultState.NO_RESULT;
    }
}
