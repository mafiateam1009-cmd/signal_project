package websocket;

import com.data_management.DataStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// these tests started as a quick check for message parsing
// they gradually expanded as we found edge cases in production logs
public class WebSocketClientTest {

    /**
     * lightweight test double so we avoid real websocket connections
     * (we had flakey CI runs when this wasn't mocked)
     */
    static class StubWebSocketClient extends WebSocketClient {

        @Override
        public void connect(DataStorage store) {
            // bypass network layer completely
            this.dataStorage = store;
        }

        @Override
        public void connect(String uri) {
            // intentionally ignored in unit tests
            // real connection tested elsewhere (integration suite)
        }
    }

    private StubWebSocketClient client;
    private DataStorage storage;

    @BeforeEach
    void setup() {

        client = new StubWebSocketClient();
        storage = new DataStorage();

        // note: keeping state clean between runs is important here
        // otherwise tests pass locally but fail in CI (learned that the hard way)
        try {
            storage.clear();
        } catch (Exception ignored) {
            // some environments don't support clear yet
        }
    }

    @Test
    void stores_valid_message() {

        client.connect(storage);

        client.onMessage("1,1700000000,HeartRate,72.5");

        var records = storage.getRecords(1, 0, Long.MAX_VALUE);

        assertNotNull(records);
        assertTrue(records.size() > 0);
    }

    @Test
    void handles_multiple_messages_same_id() {

        client.connect(storage);

        // simulating streaming updates
        client.onMessage("2,1700000001,HeartRate,70.0");
        client.onMessage("2,1700000002,HeartRate,71.2");
        client.onMessage("2,1700000003,HeartRate,72.8");

        var records = storage.getRecords(2, 0, Long.MAX_VALUE);

        // not strict ordering check yet, just existence
        assertTrue(records.size() >= 2);
    }

    @Test
    void ignores_malformed_and_empty_inputs() {

        client.connect(storage);

        // these came from real-world noisy upstream data
        client.onMessage("");
        client.onMessage(",,");
        client.onMessage("bad,data,here");
        client.onMessage(null);

        // no crash is the main requirement here
        assertTrue(true);
    }

    @Test
    void disconnect_is_safe_no_matter_state() throws IOException {

        // intentionally not connecting first
        client.disconnect();

        client.connect(storage);
        client.disconnect();
    }

    @Test
    void read_data_not_supported_in_unit_layer() {

        assertThrows(UnsupportedOperationException.class, () -> {
            client.readData(storage);
        });
    }

}