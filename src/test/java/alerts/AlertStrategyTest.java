package alerts;

import com.alerts.Strategies.BloodPressureStrategy;
import com.alerts.Strategies.OxygenSaturationStrategy;
import com.data_management.Patient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AlertStrategyTest {

    private final PrintStream originalOut = System.out;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    void oxygenSaturationStrategy_triggersOnRapidDrop() {
        Patient p = new Patient(1);
        long t = System.currentTimeMillis();
        // previous saturation 97, current 91 -> drop 6 within short window
        p.addRecord(97.0, "BloodSaturation", t - 30_000);
        p.addRecord(91.0, "BloodSaturation", t);

        OxygenSaturationStrategy strategy = new OxygenSaturationStrategy();
        strategy.checkAlert(p);

        String out = outContent.toString();
        assertTrue(out.contains("Rapid Saturation Drop") || out.contains("ALERT:"), "Expected saturation alert printed");
    }

    @Test
    void bloodPressureStrategy_triggersOnIncreasingTrend() {
        Patient p = new Patient(2);
        long t = System.currentTimeMillis();
        // create an increasing trend: 120 -> 130 -> 137 (mean change > 5)
        p.addRecord(120.0, "BloodPressure", t - 60_000);
        p.addRecord(130.0, "BloodPressure", t - 30_000);
        p.addRecord(137.0, "BloodPressure", t);

        BloodPressureStrategy strategy = new BloodPressureStrategy();
        strategy.checkAlert(p);

        String out = outContent.toString();
        assertTrue(out.contains("Increasing Blood Pressure Trend") || out.contains("Alert triggered"), "Expected blood pressure trend alert printed");
    }
}
