package com.github.militalex.frontend.analysis;

import com.github.militalex.frontend.analysis.entities.AnalysisConfig;
import com.github.militalex.frontend.analysis.entities.AnalysisStates;
import com.github.militalex.frontend.analysis.view.AnalysisView;
import com.github.militalex.microservice.AlgorithmState;
import com.github.militalex.microservice.util.OptionalEquipment;
import com.github.militalex.util.Container;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import lombok.Getter;

import java.util.Arrays;

public class AnalysisSession {
    public static int idCounter = 0;
    @Getter
    private final int sessionId;
    private final AnalysisService service;
    private final AnalysisView view;
    private final AnalysisConfig analysisConfig;
    private final AnalysisStates analysisStates;
    @Getter
    private final Binder<AnalysisConfig> configBinder;
    @Getter
    private final Binder<AnalysisStates> statesBinder;

    public AnalysisSession(AnalysisView view, AnalysisService service) {
        this.sessionId = idCounter++;
        this.service = service;
        this.view = view;
        this.analysisConfig = new AnalysisConfig();
        this.analysisStates = new AnalysisStates();
        this.configBinder = new Binder<>(AnalysisConfig.class);
        this.statesBinder = new Binder<>(AnalysisStates.class);
    }

    public boolean isUIAttached(){
        return view.getUI().isPresent() && view.getUI().get().isAttached();
    }

    public AnalysisConfig getAnalysisConfig() {
        pullFromUI();
        return analysisConfig;
    }

    public AnalysisStates getAnalysisStates() {
        pullFromUI();
        return analysisStates;
    }

    public AlgorithmState getState(OptionalEquipment optionalEquipment) {
        pullFromUI();
        return analysisStates.getState(optionalEquipment);
    }

    public Integer getResult(OptionalEquipment optionalEquipment) {
        pullFromUI();
        return analysisConfig.getResult(optionalEquipment);
    }

    public Integer getFinalResult() {
        pullFromUI();
        return analysisConfig.getFinalResult();
    }

    public void setAnalysisConfig(AnalysisService service, AnalysisConfig analysisConfig) {
        this.analysisConfig.applyFrom(analysisConfig);

        final Container<Boolean> booleanContainer = Container.of(false);
        Arrays.stream(OptionalEquipment.values()).forEach(optionalEquipment -> {
            if (analysisConfig.isResultPresent(optionalEquipment)) {
                analysisStates.setState(optionalEquipment, AlgorithmState.FINISHED);
            }
            else {
                analysisStates.setState(optionalEquipment, AlgorithmState.OFFLINE);
                booleanContainer.set(true);
            }
        });
        if (booleanContainer.get()){
            service.pingAlgorithms(sessionId);
        }
        pushToUi();
    }

    public void setState(OptionalEquipment optionalEquipment, AlgorithmState algorithmState) {
        if (analysisConfig.getResult(optionalEquipment) != null && algorithmState != AlgorithmState.RUNNING){
            return;
        }
        analysisStates.setState(optionalEquipment, algorithmState);
        pushToUi();
    }

    public void setResult(OptionalEquipment optionalEquipment, Integer result) {
        analysisConfig.setResult(optionalEquipment, result);
        analysisStates.setState(optionalEquipment, result == null ? AlgorithmState.ERROR : AlgorithmState.FINISHED);
        pushToUi();
    }

    private void readFromBeans(){
        configBinder.readBean(analysisConfig);
        statesBinder.readBean(analysisStates);
    }

    private void writeToBeans() {
        try {
            configBinder.writeBean(analysisConfig);
            statesBinder.writeBean(analysisStates);
        } catch (ValidationException e) {
            System.out.println("Konnte nicht den aktuellen Zustand aus der UI entnehmen. Es kann nur mit den gecachten Werten gearbeitet werden.");
        }
    }

    public void pullFromUI(){
        if (UI.getCurrent() != null){
            writeToBeans();
        }
        else {
            UI ui = view.getUI().orElse(null);
            if (ui == null || !ui.isAttached()) {
                service.detachSession(sessionId);
                return;
            }
            ui.access(this::writeToBeans);
        }
    }

    public void pushToUi(){
        if (UI.getCurrent() != null){
            readFromBeans();
        }
        else {
            UI ui = view.getUI().orElse(null);
            if (ui == null || !ui.isAttached()) {
                service.detachSession(sessionId);
                return;
            }
            ui.access(this::readFromBeans);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnalysisSession that)) return false;

        return sessionId == that.sessionId;
    }

    @Override
    public int hashCode() {
        return sessionId;
    }
}
