package com.github.militalex.frontend.analysis.view.components;

import com.github.militalex.module.AlgorithmState;
import com.vaadin.flow.component.AbstractCompositeField;
import com.vaadin.flow.component.html.Span;

import java.util.List;

public final class StateBadge extends AbstractCompositeField<Span, StateBadge, AlgorithmState> {

    StateBadge() {
        this(AlgorithmState.OFFLINE);
    }

    StateBadge(AlgorithmState initialState) {
        super(initialState);
        getElement().getThemeList().add("badge");
        setPresentationValue(initialState);
    }

    @Override
    protected void setPresentationValue(AlgorithmState state) {
        getContent().removeClassName("red");
        getContent().setText(state.getText());
        clearThemes();
        getElement().getThemeList().addAll(List.of(state.getThemes()));
        if (state == AlgorithmState.ERROR){
            getContent().addClassName("red");
        }
    }

    private void clearThemes() {
        for (AlgorithmState value : AlgorithmState.values()) {
            List.of(value.getThemes()).forEach(getElement().getThemeList()::remove);
        }
    }
}
