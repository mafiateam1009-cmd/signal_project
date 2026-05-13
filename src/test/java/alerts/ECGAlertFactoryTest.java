package alerts;

import com.alerts.Alert;
import com.alerts.ECGAlertFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ECGAlertFactoryTest {
    @Test
    public void testCreatesCorrectAlertTypesTest() {
        ECGAlertFactory factory = new ECGAlertFactory();
        Alert alert = factory.createAlert("6443029", "Dying", 1231233423);
        assertNotNull(alert);
        assertInstanceOf(ECGAlertFactoryTest.class, alert);
    }
}
