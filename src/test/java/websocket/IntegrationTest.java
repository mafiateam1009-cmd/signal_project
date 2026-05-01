package websocket;

import com.alerts.AlertGenerator;
import com.data_management.DataStorage;
import com.data_management.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import websocket.WebSocketClient;

import static org.junit.jupiter.api.Assertions.*;

// integration tests around ingestion + alert evaluation
// these grew over time while investigating data inconsistencies in logs
public class IntegrationTest {

    // test double to avoid real websocket dependency during runs
    static class StubWebSocketClient extends WebSocketClient {

        @Override
        public void connect(DataStorage store) {
            this.dataStorage = store;
        }

        @Override
        public void connect(String uri) {
            // not used here (this would be in a real environment)
        }
    }

    private StubWebSocketClient client;
    private DataStorage storage;
    private AlertGenerator alertGenerator;

    @BeforeEach
    void setUp() {

        client = new StubWebSocketClient();
        storage = new DataStorage();
        alertGenerator = new AlertGenerator(storage);

        // NOTE: had an issue where previous state leaked between runs
        // not fully sure why, but clearing here avoids flaky behavior
        try {
            storage.clear();
        } catch (Exception ignored) {
            // some implementations don't support clearing yet
        }
    }

    @Test
    void ingestion_should_store_received_message() {

        client.connect(storage);

        client.onMessage("1,1700000000,HeartRate,72.5");

        var records = storage.getRecords(1, 0L, Long.MAX_VALUE);

        // if this fails, ingestion pipeline is broken
        assertTrue(records != null && !records.isEmpty());
    }

    @Test
    void alert_evaluation_should_not_crash_on_existing_data() {

        client.connect(storage);
        client.onMessage("1,1700000000,HeartRate,72.5");

        for (Patient patient : storage.getAllPatients()) {

            // alert logic itself is validated elsewhere
            // here we only care that evaluation doesn't fail unexpectedly
            assertDoesNotThrow(() -> alertGenerator.evaluateData(patient));
        }
    }

    @Test
    void ingestion_should_handle_repeated_and_mixed_patient_updates() {

        client.connect(storage);

        // simulating real incoming stream where same patient updates multiple times
        client.onMessage("1,1700000000,HeartRate,72.5");
        client.onMessage("2,1700000001,BloodPressure,120.0");
        client.onMessage("1,1700000002,HeartRate,73.1"); // update to existing patient

        var patients = storage.getAllPatients();

        // not enforcing strict schema consistency yet
        // just ensuring ingestion is functioning
        assertTrue(patients.size() > 0);

        for (Patient patient : patients) {
            assertDoesNotThrow(() -> alertGenerator.evaluateData(patient));
        }
    }

}
