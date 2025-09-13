package com.github.militalex.module.fuel;

import com.github.militalex.module.ModuleService;
import com.github.militalex.module.Modules;
import com.github.militalex.module.util.OptionalEquipment;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class FuelModuleService extends ModuleService {

    private static final Random RANDOM = new Random();

    public FuelModuleService() {
        super(Modules.FUEL_MANAGEMENT);
    }

    @Override
    protected Integer runAnalysis(OptionalEquipment optionalEquipment, String entry, Object unused) {
        long sleepTime = RANDOM.nextInt(14001) + 2000;
        try {
            Thread.sleep(sleepTime);
            System.out.println("Analyse abgeschlossen, Sleep Time: " + sleepTime + "ms");
            return generateNumberOrNull();
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
