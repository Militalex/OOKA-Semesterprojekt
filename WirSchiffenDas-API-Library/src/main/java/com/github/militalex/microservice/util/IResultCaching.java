package com.github.militalex.microservice.util;

import java.util.Collection;
import java.util.List;

public interface IResultCaching {
    Collection<Integer> sessionsSet();
    Collection<List<AlgorithmResult>> values();
    boolean containsSession(int sessionId);
    boolean isPresent(int sessionId, OptionalEquipment optionalEquipment);
    boolean isPresent(OptionalEquipment optionalEquipment);
    boolean isPresent(OptionalEquipment optionalEquipment, String entry);
    void cacheSession(int sessionId, OptionalEquipment optionalEquipment, String entry);
    void cacheSession(int sessionId, AlgorithmResult emptyAlgoResult);
    AlgorithmResult cacheResult(int sessionId, OptionalEquipment optionalEquipment, Integer result);
    boolean isResultPresent(int sessionId, OptionalEquipment optionalEquipment);
    AlgorithmResult peekResult(int sessionId, OptionalEquipment optionalEquipment);
    AlgorithmResult takeResult(int sessionId, OptionalEquipment optionalEquipment);
    void clearCache();
}
