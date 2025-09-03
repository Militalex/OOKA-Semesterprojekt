package com.github.militalex.frontend.analysis;

import com.github.militalex.frontend.analysis.entities.AnalysisConfig;
import com.github.militalex.frontend.analysis.entities.AnalysisConfigRepos;
import com.github.militalex.frontend.analysis.view.AnalysisView;
import com.github.militalex.microservice.AlgorithmState;
import com.github.militalex.microservice.util.OptionalEquipment;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class AnalysisService {

    private final HashMap<Integer, AnalysisSession> sessions = new HashMap<>();
    private final AnalysisConfigRepos configurationRepository;

    @Setter
    private AnalysisPort port;

    public AnalysisService(AnalysisConfigRepos configurationRepository) {
        this.configurationRepository = configurationRepository;
    }

    public AnalysisSession createSession(AnalysisView view){
        AnalysisSession session = new AnalysisSession(view, this);
        sessions.put(session.getSessionId(), session);
        clearDetachedSessions(session.getSessionId());
        return session;
    }

    public boolean isSessionOpen(int sessionId){
        return sessions.containsKey(sessionId);
    }

    public void detachSession(int sessionId){
        sessions.remove(sessionId);
    }

    private void clearDetachedSessions(int newCreatedSessionId){
        sessions.values().stream().filter(analysisSession -> analysisSession.getSessionId() != newCreatedSessionId)
                .filter(analysisSession -> !analysisSession.isUIAttached()).toList()
                .forEach(analysisSession -> sessions.remove(analysisSession.getSessionId()));
    }

    public void pingAlgorithms(int sessionId) {
        if (port == null) {
            throw new IllegalStateException("AnalysisPort is not set. Please set it before calling pingAlgorithms.");
        }
        port.pingAlgorithms(sessionId);
    }

    public AnalysisConfig getAnalysisConfig(int sessionId) {
        return sessions.get(sessionId).getAnalysisConfig();
    }

    public AlgorithmState getState(int sessionId, OptionalEquipment optionalEquipment) {
        return sessions.get(sessionId).getState(optionalEquipment);
    }

    public Integer getResult(int sessionId, OptionalEquipment optionalEquipment) {
        return sessions.get(sessionId).getResult(optionalEquipment);
    }

    public Integer getFinalResult(int sessionId) {
        return sessions.get(sessionId).getFinalResult();
    }

    public void setState(@Nullable Integer sessionId, OptionalEquipment optionalEquipment, AlgorithmState algorithmState) {
        if (sessionId == null){
            sessions.values().stream()
                    .filter(analysisSession -> !List.of(AlgorithmState.FINISHED, AlgorithmState.ERROR).contains(analysisSession.getState(optionalEquipment)))
                    .forEach(analysisSession -> analysisSession.setState(optionalEquipment, algorithmState));
        }
        else {
            sessions.get(sessionId).setState(optionalEquipment, algorithmState);
        }
    }

    public void setResult(int sessionId, OptionalEquipment optionalEquipment, Integer result) {
        if (!sessions.containsKey(sessionId)){
            return;
        }
        sessions.get(sessionId).setResult(optionalEquipment, result);
    }

    public void requestAnalysis(int sessionId, OptionalEquipment optionalEquipment){
        if (port == null) {
            throw new IllegalStateException("AnalysisPort is not set. Please set it before calling requestAnalysis.");
        }
        setResult(sessionId, optionalEquipment, null);
        setState(sessionId, optionalEquipment, AlgorithmState.RUNNING);
        port.requestAnalysis(sessionId, optionalEquipment, sessions.get(sessionId).getAnalysisConfig().getEntry(optionalEquipment));
    }

    public void saveConfig(int sessionId) {
        configurationRepository.save(sessions.get(sessionId).getAnalysisConfig());
    }

    public void loadConfigByID(int sessionId, long db_id) {
        sessions.get(sessionId).setAnalysisConfig(this, configurationRepository.findById(db_id).orElseThrow());
    }

    public List<AnalysisConfig> loadAll() {
        return configurationRepository.findAll();
    }
}
