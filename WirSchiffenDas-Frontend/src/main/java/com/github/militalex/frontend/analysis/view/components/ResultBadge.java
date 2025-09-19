package com.github.militalex.frontend.analysis.view.components;

import com.vaadin.flow.component.icon.Icon;
import lombok.Getter;

public class ResultBadge extends Icon {

    @Getter
    private ResultState state;

    public ResultBadge() {
        getElement().getThemeList().add("badge");
        setState(ResultState.NO_RESULT);
    }

    public ResultBadge(ResultState initialState) {
        setState(initialState);
    }

    public void setState(ResultState state) {
        setIcon(state.getIcon());
        clearThemes();
        if (state.getTheme() != null) {
            getElement().getThemeList().add(state.getTheme());
        }
        if (state.equals(ResultState.PARTIAL_PASSED)){
            addClassName("orange");
        }
        this.state = state;
    }

    private void clearThemes() {
        removeClassName("orange");
        for (ResultState value : ResultState.values()) {
            getElement().getThemeList().remove(value.getTheme());
        }
    }
}
