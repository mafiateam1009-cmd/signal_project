package data_management;

import com.alerts.Alert;
import com.alerts.BloodOxigenAlert;
import com.alerts.BloodOxygenAlertFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BloodOxygenAlertFactoryTest {
    @Test
    public void testCreatesCorrectAlertTypesTest() {
        BloodOxygenAlertFactory factory = new BloodOxygenAlertFactory();
        Alert alert = factory.createAlert("6443029", "Bad", 1213232321);
        assertNotNull(alert);
        assertInstanceOf(BloodOxigenAlert.class, alert);
    }
}
