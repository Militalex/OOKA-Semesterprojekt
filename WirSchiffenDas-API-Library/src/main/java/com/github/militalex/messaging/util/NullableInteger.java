package com.github.militalex.messaging.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.Nullable;

public class NullableInteger {
    private final String value;

    public NullableInteger(@Nullable Integer value) {
        this.value = "" + value;
    }

    @JsonCreator
    public NullableInteger(@JsonProperty("value") String value) {
        this.value = (value == null || value.equals("null")) ? "null" : (Integer.parseInt(value) + "");
    }

    public @Nullable Integer getValue() {
        if (value == null || value.equals("null"))
            return null;
        return Integer.parseInt(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
