package alerts;

import org.junit.jupiter.api.Test;

import com.alerts.Alert;
import com.alerts.Decorator.PriorityAlertDecorator;
import com.alerts.Decorator.RepeatedAlertDecorator;

public class AlertDecoratorTest {
    
    @Test
    void testPriorityAlertDecorator() {
        Alert alert = new Alert("patient_1", "HighBloodPressure", 123456789L);
        PriorityAlertDecorator priorityAlert = new PriorityAlertDecorator(alert, "High");
        System.out.println(priorityAlert.toString());
        assert(priorityAlert.getPriorityLabel().equals("High"));
    }

    @Test
    void testPriorityAlertDecoratorWithoutLabel() {
        Alert alert = new Alert("patient_2", "LowOxygen", 987654321L);
        PriorityAlertDecorator priorityAlert = new PriorityAlertDecorator(alert);
        priorityAlert.setPriorityLabel("Medium");
        System.out.println(priorityAlert.toString());
        assert(priorityAlert.getPriorityLabel().equals("Medium"));
    }

    @Test
    void testRepeatedAlertDecorator() {
        Alert alert = new Alert("patient_3", "AbnormalECG", 567890123L);
        PriorityAlertDecorator priorityAlert = new PriorityAlertDecorator(alert, "Critical");
        RepeatedAlertDecorator repeatedAlert = new RepeatedAlertDecorator(priorityAlert, 3);
        System.out.println(repeatedAlert.toString());
        assert(repeatedAlert.getRepeatCount() == 3);
    }   
}
