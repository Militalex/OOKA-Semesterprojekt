package com.github.militalex.microservice.util;

import com.github.militalex.messaging.util.NullableInteger;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.MultiValueMapAdapter;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.*;

public final class ListResultCache implements IResultCaching {

    private final MultiValueMapAdapter<Integer, AlgorithmResult> cache = new MultiValueMapAdapter<>(Collections.synchronizedMap(new HashMap<>()));

    @Override
    public synchronized boolean containsSession(int sessionId) {
        fixCache();
        return cache.containsKey(sessionId);
    }

    @Override
    public synchronized boolean isPresent(int sessionId, OptionalEquipment optionalEquipment) {
        fixCache();
        return containsSession(sessionId) && cache.get(sessionId).stream()
                .anyMatch(ar -> ar.getOptionalEquipment() == optionalEquipment);
    }

    @Override
    public synchronized Collection<Integer> sessionsSet() {
        fixCache();
        return cache.keySet();
    }

    @Override
    public synchronized Collection<List<AlgorithmResult>> values() {
        fixCache();
        return cache.values();
    }

    @Override
    public synchronized boolean isPresent(OptionalEquipment optionalEquipment) {
        fixCache();
        return sessionsSet().stream()
                .anyMatch(sessionId -> isPresent(sessionId, optionalEquipment));
    }

    @Override
    public synchronized boolean isPresent(OptionalEquipment optionalEquipment, String entry) {
        fixCache();
        return sessionsSet().stream().map(sessionId -> peekResult(sessionId, optionalEquipment))
                .anyMatch(ar -> ar.getEntry().equals(entry));
    }

    @Override
    public synchronized void cacheSession(int sessionId, OptionalEquipment optionalEquipment, String entry) {
        fixCache();
        if (isPresent(sessionId, optionalEquipment)){
            return;
        }
        cache.add(sessionId, new AlgorithmResult(sessionId, optionalEquipment, entry));
    }

    @Override
    public synchronized void cacheSession(int sessionId, AlgorithmResult emptyAlgoResult) {
        fixCache();
        if (emptyAlgoResult.isResultPresent()){
            throw new IllegalArgumentException("Provided AlgorithmResult is not empty." );
        }
        if (emptyAlgoResult == null || isPresent(sessionId, emptyAlgoResult.getOptionalEquipment())){
            return;
        }
        cache.add(sessionId, emptyAlgoResult);
    }

    @Override
    public synchronized AlgorithmResult cacheResult(int sessionId, OptionalEquipment optionalEquipment, Integer result) {
        fixCache();
        AlgorithmResult algorithmResult = peekResult(sessionId, optionalEquipment);
        algorithmResult.setResult(new NullableInteger(result));

        return algorithmResult;
    }

    @Override
    public synchronized boolean isResultPresent(int sessionId, OptionalEquipment optionalEquipment) {
        fixCache();
        return peekResult(sessionId, optionalEquipment).isResultPresent();
    }

    @Override
    public synchronized AlgorithmResult peekResult(int sessionId, OptionalEquipment optionalEquipment) {
        fixCache();
        if (!isPresent(sessionId, optionalEquipment)){
            throw new IllegalStateException("No session with ID " + sessionId + " cached.");
        }
        return cache.get(sessionId).stream()
                .filter(Objects::nonNull)
                .filter(algorithmResult -> algorithmResult.getOptionalEquipment() == optionalEquipment).findFirst().orElseThrow();
    }

    @Override
    public synchronized AlgorithmResult takeResult(int sessionId, OptionalEquipment optionalEquipment) {
        fixCache();
        if (!cache.containsKey(sessionId))
            throw new IllegalStateException("No session with ID " + sessionId + " cached.");


        AlgorithmResult result = peekResult(sessionId, optionalEquipment);
        cache.get(sessionId).remove(result);
        if (cache.get(sessionId).isEmpty())
            cache.remove(sessionId);
        return result;
    }

    @Override
    public synchronized void clearCache() {
        fixCache();
        cache.clear();
    }

    private synchronized void fixCache(){
        cache.keySet().stream()
                .filter(sessionId -> cache.get(sessionId).size() < 0)
                .forEach(cache::remove);
        cache.keySet().stream()
                .map(cache::get)
                .forEach(algorithmResults -> algorithmResults.removeIf(Objects::isNull));
        cache.keySet().stream()
                .filter(sessionId -> cache.get(sessionId).size() < 0)
                .forEach(cache::remove);
    }
}
