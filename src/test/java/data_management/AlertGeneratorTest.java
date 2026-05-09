package data_management;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.alerts.Alert;
import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class AlertGeneratorTest {

    private AlertGenerator alertGenerator;
    private DataStorage mockDataStorage;
    private Patient testPatient;

    @BeforeEach
    void setUp() {
        // Create a mock DataStorage
        mockDataStorage = mock(DataStorage.class);
        // Create an AlertGenerator with the mocked DataStorage
        alertGenerator = new AlertGenerator(mockDataStorage);
        // Create a test patient
        testPatient = new Patient(1);
    }

    /**
     * Helper method to capture System.out to verify alerts are printed
     */
    private String captureSystemOutput(Runnable action) {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        action.run();
        System.setOut(System.out);
        return outContent.toString();
    }

    @Test
    void testCheckBloodPressureIncreasingTrendDetectsIncrease() {
        // Arrange: Add blood pressure records with values that should trigger alert
        testPatient.addRecord(110.0, "BloodPressure", 1000L);
        testPatient.addRecord(115.0, "BloodPressure", 2000L);
        testPatient.addRecord(120.0, "BloodPressure", 3000L);
        testPatient.addRecord(135.0, "BloodPressure", 4000L);

        // Act: Call the method with a threshold of 9.5 and window size of 3
        // Window 1: [110, 115, 120] has mean change of 5.0 (no alert)
        // Window 2: [115, 120, 135] has mean change of 10.0 (> 9.5 - should trigger alert)
        String output = captureSystemOutput(() -> 
            alertGenerator.checkBloodPressureIncreasingTrend(testPatient, 9.5, 3)
        );

        // Assert: Verify that an alert was triggered
        assertTrue(output.toLowerCase().contains("alert"), "Alert should be triggered for increasing trend in blood pressure");
        assertTrue(output.toLowerCase().contains("Increasing Blood Pressure Trend".toLowerCase()), "Alert condition should indicate increasing blood pressure trend");
    }

    @Test
    void testCheckBloodPressureIncreasingTrendNoAlertBelowThreshold() {
        // Arrange: Add blood pressure records all below the threshold
        testPatient.addRecord(90.0, "BloodPressure", 1000L);
        testPatient.addRecord(95.0, "BloodPressure", 2000L);
        testPatient.addRecord(100.0, "BloodPressure", 3000L);
        testPatient.addRecord(105.0, "BloodPressure", 4000L);

        // Act: Call the method with threshold of 120.0 and window size of 3
        // All windows: means are 5.0, 5, 100.0 (all below 10.0 - no alert)
        String output = captureSystemOutput(() -> 
            alertGenerator.checkBloodPressureIncreasingTrend(testPatient, 10, 3)
        );

        // Assert: No alert should be triggered
        assertFalse(output.toLowerCase().contains("alert"), "No alert should be triggered when all values are below threshold");

    }

    @Test
    void testCheckBloodPressureIncreasingTrendWithSmallWindow() {
        // Arrange: Add blood pressure records with high values
        testPatient.addRecord(140.0, "BloodPressure", 1000L);
        testPatient.addRecord(145.0, "BloodPressure", 2000L);

        // Act: Call the method with window size of 3 and threshold of 10.0
        String output = captureSystemOutput(() -> 
            alertGenerator.checkBloodPressureIncreasingTrend(testPatient, 10.0, 3)
        );

        // Assert: Should not trigger alert as number of records less than window size.
        assertFalse(output.toLowerCase().contains("alert"), "No alert should be triggered when there are fewer records than the window size");
    }

    @Test
    void testCheckBloodPressureDecreasingTrendDetectsDecrease() {
        // Arrange: Add blood pressure records with values that should trigger alert
        testPatient.addRecord(140.0, "BloodPressure", 1000L);
        testPatient.addRecord(135.0, "BloodPressure", 2000L);
        testPatient.addRecord(130.0, "BloodPressure", 3000L);
        testPatient.addRecord(115.0, "BloodPressure", 4000L);

        // Act: Call the method with a threshold of 9.5 and window size of 3
        // Window 1: [140, 135, 130] has mean change of -5.0 (no alert)
        // Window 2: [135, 130, 115] has mean change of -10.0 (< -9.5 - should trigger alert)
        String output = captureSystemOutput(() -> 
            alertGenerator.checkBloodPressureDecreasingTrend(testPatient, 9.5, 3)
        );

        // Assert: Verify that an alert was triggered
        assertTrue(output.toLowerCase().contains("alert"), "Alert should be triggered for decreasing trend in blood pressure");
        assertTrue(output.toLowerCase().contains("Decreasing Blood Pressure Trend".toLowerCase()), "Alert condition should indicate decreasing blood pressure trend");
    }

    @Test
    void testCheckBloodPressureAboveThreshold() {
        // Arrange: Add blood pressure records with values that should trigger alert
        testPatient.addRecord(190.0, "BloodPressure", 1000L); // Systolic above 180
        testPatient.addRecord(85.0, "BloodPressure", 2000L); // Systolic below 90
        testPatient.addRecord(125.0, "BloodPressure", 3000L); // Diastolic above 120
        testPatient.addRecord(55.0, "BloodPressure", 4000L); // Diastolic below 60

        // Act: Call the method to check critical blood pressure thresholds
        String output = captureSystemOutput(() -> 
            alertGenerator.criticalBloodPressureThresholdCheck(testPatient)
        );

        // Assert: Verify that alerts were triggered for all critical values
        assertTrue(output.toLowerCase().contains("alert"), "Alert should be triggered for critical blood pressure values");
        assertTrue(output.toLowerCase().contains("systolic".toLowerCase()), "Alert should indicate systolic blood pressure issue");
        assertTrue(output.toLowerCase().contains("diastolic".toLowerCase()), "Alert should indicate diastolic blood pressure issue");
    }

    @Test
    void testHypotensiveHypoxemiaAlertCheck() {
        // Arrange: Add blood pressure and oxygen saturation records with values that should trigger alert
        testPatient.addRecord(85.0, "BloodPressure", 1000L); // BP below 90
        testPatient.addRecord(55.0, "BloodPressure", 2000L); // BP below 90
        testPatient.addRecord(88.0, "BloodSaturation", 2000L); // Oxygen saturation below 90%

        // Act: Call the method to check for hypotension and hypoxemia
        String output = captureSystemOutput(() -> {
            alertGenerator.hypotensiveHypoxemiaCheck(testPatient);
        });

        // Assert: Verify that an alert was triggered for hypotension and hypoxemia
        assertTrue(output.toLowerCase().contains("alert"), "Alert should be triggered for hypotension and hypoxemia");
        assertTrue(output.toLowerCase().contains("Hypotensive Hypoxemia Detected".toLowerCase()), "Alert should indicate hypotensive hypoxemia issue");
    }
    
}
