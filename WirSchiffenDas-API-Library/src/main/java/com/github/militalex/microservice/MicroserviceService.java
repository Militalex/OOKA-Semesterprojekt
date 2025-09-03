package com.github.militalex.microservice;

import com.github.militalex.microservice.util.OptionalEquipment;
import lombok.Setter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

import java.util.concurrent.CompletableFuture;

public abstract class MicroserviceService {
    protected final Microservice microservice;

    @Setter
    protected MicroservicePort port;

    public MicroserviceService(Microservice microservice) {
        this.microservice = microservice;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onContextOpened() {
        microservice.getOptionalEquipments().forEach(optionalEquipment ->
                port.sendAlgorithmState(null, optionalEquipment, AlgorithmState.READY));
    }

    @EventListener(ContextClosedEvent.class)
    public void onContextClosed(){
        microservice.getOptionalEquipments().forEach(optionalEquipment ->
                port.sendAlgorithmState(null, optionalEquipment, AlgorithmState.OFFLINE));
    }

    public void startAnalysis(int sessionId, OptionalEquipment optionalEquipment, String entry) {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> runAnalysis(optionalEquipment, entry, null));
        future.whenComplete((result, exception) -> {
            if (exception != null) {
                setAnalysisResult(sessionId, optionalEquipment, entry, null);
            } else {
                setAnalysisResult(sessionId, optionalEquipment, entry, result);
            }
        });
    }

    protected abstract Integer runAnalysis(OptionalEquipment optionalEquipment, String entry, Object object);

    protected void setAnalysisResult(int sessionId, OptionalEquipment optionalEquipment, String entry, Integer result) {
        port.sendAlgorithmResultToFrontend(sessionId, optionalEquipment, entry, result);
    }
}
