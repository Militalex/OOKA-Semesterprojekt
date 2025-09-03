package com.github.militalex.frontend.analysis.view.components;

import com.vaadin.flow.component.icon.Icon;

public class ResultBadge extends Icon {

    private ResultState state;

    public ResultBadge() {
        getElement().getThemeList().add("badge");
        setState(ResultState.NO_RESULT);
    }

    public ResultBadge(ResultState initialState) {
        setState(initialState);
    }

    public void setNoResult() {
        setState(ResultState.NO_RESULT);
    }

    public void setPassed() {
        setState(ResultState.PASSED);
    }

    public void setFailed() {
        setState(ResultState.FAILED);
    }

    public void setPartialPass() {
        setState(ResultState.PARTIAL_PASSED);
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
