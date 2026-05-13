package outputs;

import static org.junit.jupiter.api.Assertions.*;

import com.cardio_generator.HealthDataSimulator;
import org.junit.jupiter.api.Test;

public class HealthDataSimulatorTest {

    @Test
    void singletonReturnsTheSameInstanceTest(){
        HealthDataSimulator instance1 = HealthDataSimulator.getInstance();
        HealthDataSimulator instance2 = HealthDataSimulator.getInstance();
        assertSame(instance1, instance2);
    }
}
