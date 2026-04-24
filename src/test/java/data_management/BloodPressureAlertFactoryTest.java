package data_management;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.alerts.AlertFactory;
import com.alerts.BloodPressureAlertFactory;


import com.alerts.Alert;
import com.alerts.BloodPressureAlert;

public class BloodPressureAlertFactoryTest {
    // BloodPressureAlertFactoryTest.java
    @Test
    void testCreatesCorrectAlertTypeTest() {
        AlertFactory factory = new BloodPressureAlertFactory();
        Alert alert = factory.createAlert("6443029", "Bad", 1213232321);
        assertNotNull(alert);
        assertInstanceOf(BloodPressureAlert.class, alert);
    }
}
