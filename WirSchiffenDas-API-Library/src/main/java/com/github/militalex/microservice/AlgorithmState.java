package com.github.militalex.microservice;

import lombok.Getter;

@Getter
public enum AlgorithmState {
    OFFLINE("Offline", "contrast"),
    READY("Ready", "success"),
    RUNNING("Running"),
    FINISHED("Finished", "success", "primary"),
    ERROR("Error", "error");

    private final String text;
    private final String[] themes;

    AlgorithmState(String text, String... themes) {
        this.text = text;
        this.themes = themes;
    }
}
