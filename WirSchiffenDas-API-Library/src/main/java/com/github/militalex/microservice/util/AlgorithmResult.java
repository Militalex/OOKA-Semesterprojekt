package com.github.militalex.microservice.util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.militalex.messaging.util.NullableInteger;
import lombok.Getter;
import lombok.Setter;

@Getter
public class AlgorithmResult {
    private final int sessionId;
    private final OptionalEquipment optionalEquipment;
    @Setter
    private String entry;
    @Setter
    private NullableInteger result;

    public AlgorithmResult(int sessionId, OptionalEquipment optionalEquipment, String entry) {
        this(sessionId, optionalEquipment, entry, null);
    }

    @JsonCreator
    public AlgorithmResult(@JsonProperty("sessionId") int sessionId,
                           @JsonProperty("optionalEquipment") OptionalEquipment optionalEquipment,
                           @JsonProperty("entry") String entry,
                           @JsonProperty("result") NullableInteger result) {
        this.sessionId = sessionId;
        this.optionalEquipment = optionalEquipment;
        this.entry = entry;
        this.result = result;
    }

    public boolean isResultPresent(){
        return result != null;
    }

    @Override
    public String toString() {
        return "AlgorithmResult{" +
                "sessionId=" + sessionId +
                ", optionalEquipment=" + optionalEquipment +
                ", entry='" + entry + '\'' +
                ", result=" + result +
                '}';
    }
}
