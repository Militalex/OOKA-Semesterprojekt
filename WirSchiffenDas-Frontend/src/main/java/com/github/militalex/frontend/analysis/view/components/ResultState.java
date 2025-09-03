package com.github.militalex.frontend.analysis.view.components;

import com.vaadin.flow.component.icon.VaadinIcon;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor @Getter
public enum ResultState {
    NO_RESULT("- Noch nicht vorhanden -", VaadinIcon.MINUS, "contrast"),
    FAILED("Failed", VaadinIcon.CLOSE_SMALL, "error"),
    PARTIAL_PASSED("Partial Passed", VaadinIcon.MINUS, null),
    PASSED("Passed", VaadinIcon.CHECK, "success");

    private final String text;
    private final VaadinIcon icon;
    @Nullable
    private final String theme;
}
