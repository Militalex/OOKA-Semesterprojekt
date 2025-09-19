package com.github.militalex.module;

import com.github.militalex.module.util.OptionalEquipment;
import lombok.Setter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

import java.util.concurrent.CompletableFuture;

public abstract class ModuleService {
    protected final Modules module;

    @Setter
    protected ModulePort port;

    public ModuleService(Modules module) {
        this.module = module;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onContextOpened() {
        module.getOptionalEquipments().forEach(optionalEquipment ->
                port.sendAlgorithmState(null, optionalEquipment, AlgorithmState.READY));
    }

    @EventListener(ContextClosedEvent.class)
    public void onContextClosed(){
        module.getOptionalEquipments().forEach(optionalEquipment ->
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
