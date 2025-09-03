package com.github.militalex.microservice.enginemng;

import com.github.militalex.messaging.util.NullableInteger;
import com.github.militalex.microservice.AlgorithmState;
import com.github.militalex.microservice.Microservice;
import com.github.militalex.microservice.MicroserviceService;
import com.github.militalex.microservice.util.AlgorithmResult;
import com.github.militalex.microservice.util.OptionalEquipment;
import com.github.militalex.microservice.util.ResultsPackage;
import lombok.Setter;
import org.apache.kafka.common.errors.TimeoutException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

@Service
public class EngineMngMicroserviceService extends MicroserviceService {

    private static final Random RANDOM = new Random();

    @Setter
    private EngineMngMicroservicePort subPort;

    public EngineMngMicroserviceService() {
        super(Microservice.ENGINE_MANAGEMENT_MICROSERVICE);
    }

    @Override
    public void startAnalysis(int sessionId, OptionalEquipment optionalEquipment, String entry) {
        this.port.sendAlgorithmState(sessionId, optionalEquipment, AlgorithmState.RUNNING);
        subPort.requestResults(sessionId);

        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            int maxSeconds = 60;
            for (int i = 0; i < maxSeconds; i++) {
                if (subPort.isComplete(sessionId)) {
                    ResultsPackage resultsPackage = subPort.takeResults(sessionId);
                    return this.runAnalysis(optionalEquipment, entry, resultsPackage);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Warten auf Ergebnisse wurde unterbrochen: " + e.getMessage());
                    throw new RuntimeException(e);
                }
            }
            throw new TimeoutException("Timeout: Not all results were ready after " + maxSeconds + " seconds.");
        });
        future.whenComplete((result, exception) -> {
            if (exception != null) {
                this.setAnalysisResult(sessionId, optionalEquipment, entry, null);
            } else {
                this.setAnalysisResult(sessionId, optionalEquipment, entry, result);
            }
        });
    }

    @Override
    protected Integer runAnalysis(OptionalEquipment optionalEquipment, String entry, Object resultPackage) {
        ResultsPackage resultsPackage = (ResultsPackage) resultPackage;

        long sleepTime = RANDOM.nextInt(5001) + 2000;
        try {
            Thread.sleep(sleepTime);
            System.out.println("Analyse abgeschlossen, Sleep Time: " + sleepTime + "ms");

            Integer result = generateNumberOrNull();
            if (result == null)
                return null;
            int sum = Arrays.stream(OptionalEquipment.values()).filter(oe -> oe != OptionalEquipment.ENGINE_MANAGEMENT_SYSTEM)
                    .map(oe -> resultsPackage.getResult(oe).getResult())
                    .filter(Objects::nonNull)
                    .map(NullableInteger::getValue)
                    .filter(Objects::nonNull)
                    .mapToInt(Integer::intValue)
                    .sum() + result;
            return sum / OptionalEquipment.values().length;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Analyse wurde unterbrochen: " + e.getMessage());
            return null;
        }
    }

    /**
     * Generates a random integer between 0 and 100, inclusive.
     * In 5% of cases, returns null.
     *
     * @author Google Gemini AI
     * @return A random integer (0-100) or null.
     */
    public Integer generateNumberOrNull() {
        // Generiere eine zufällige Zahl von 0 bis 99
        int chance = RANDOM.nextInt(100);

        // Prüfe, ob die Zahl im 5 % Bereich liegt (z. B. 0 bis 4)
        if (chance < 5) {
            return null; // 5 % der Fälle
        } else {
            // Generiere eine zufällige Zahl von 0 bis 100
            return RANDOM.nextInt(101);
        }
    }
}
