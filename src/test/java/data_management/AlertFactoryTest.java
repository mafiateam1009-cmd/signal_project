package data_management;

import static org.junit.jupiter.api.Assertions.*;

import com.alerts.*;
import org.junit.jupiter.api.Test;

public class AlertFactoryTest {

    @Test
    void testBloodPressureFactoryCreatesAlert() {
        AlertFactory factory = new BloodPressureAlertFactory();
        Alert alert = factory.createAlert("patient_1", "HighBloodPressure", 123456789L);
        assertNotNull(alert);
    }

    @Test
    void testBloodOxygenFactoryCreatesAlert() {
        AlertFactory factory = new BloodOxygenAlertFactory();
        Alert alert = factory.createAlert("patient_1", "LowOxygen", 123456789L);
        assertNotNull(alert);
    }

    @Test
    void testECGFactoryCreatesAlert() {
        AlertFactory factory = new ECGAlertFactory();
        Alert alert = factory.createAlert("patient_1", "AbnormalECG", 123456789L);
        assertNotNull(alert);
    }
    
    }


