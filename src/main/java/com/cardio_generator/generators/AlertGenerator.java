package com.cardio_generator.generators;

import java.util.Random;

import com.cardio_generator.outputs.OutputStrategy;

/**
 * This class generates alert data for patients.
 * It tracks whether an alert is currently active or not so it can decide
 * whether to trigger a new one or resolve an existing one.
 */
public class AlertGenerator implements PatientDataGenerator {

    public static final Random randomGenerator = new Random();
    private boolean[] AlertStates; // false = resolved, true = pressed

    /**
     * Constructor that initializes the alert states for all patients.
     * All patients start with no active alerts (false).
     * @param patientCount the total number of patients
     */
    public AlertGenerator(int patientCount) {
        AlertStates = new boolean[patientCount + 1];
    }

    /**
     * This method decides if an alert should be triggered or resolved.
     * If an alert is active, there is a high chance it gets resolved.
     * If no alert is active, it uses a probability formula to see if a new one starts.
     * @param patientId the ID of the patient
     * @param outputStrategy the interface used to send the alert status
     */
    @Override
    public void generate(int patientId, OutputStrategy outputStrategy) {
        try {
            if (AlertStates[patientId]) {
                // If there's an active alert, 90% of the time it will be resolved
                if (randomGenerator.nextDouble() < 0.9) {
                    AlertStates[patientId] = false;
                    // Send "resolved" status
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "resolved");
                }
            } else {
                // Math to figure out if a new alert should pop up
                double Lambda = 0.1; // Average rate of alerts
                double p = -Math.expm1(-Lambda); // Probability of an alert happening
                boolean alertTriggered = randomGenerator.nextDouble() < p;

                if (alertTriggered) {
                    AlertStates[patientId] = true;
                    // Send "triggered" status
                    outputStrategy.output(patientId, System.currentTimeMillis(), "Alert", "triggered");
                }
            }
        } catch (Exception e) {
            // Print error if something goes wrong during the random generation
            System.err.println("An error occurred while generating alert data for patient " + patientId);
            e.printStackTrace();
        }
    }
}