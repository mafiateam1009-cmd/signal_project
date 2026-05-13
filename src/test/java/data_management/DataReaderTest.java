package data_management;

import com.data_management.DataReader;
import com.data_management.DataStorage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// tests for DataReader
// added gradually as ingestion logic evolved and edge cases started appearing in logs
public class DataReaderTest {

    @Test
    void basic_ingestion_should_store_record() throws Exception {

        DataStorage storage = new DataStorage();

        // simplified test reader - not a full implementation
        DataReader reader = new DataReader() {

            @Override
            public void readData(DataStorage ds) {
                ds.addPatientData(1, 72.5, "HeartRate", 1700000000L);
            }

            @Override
            public void connect(DataStorage ds) {
                // not used in this context
            }

            @Override
            public void disconnect() {
                // ignored for unit tests
            }
        };

        reader.readData(storage);

        var records = storage.getRecords(1, 0, Long.MAX_VALUE);

        // basic sanity check - full validation not implemented yet
        assertFalse(records == null || records.isEmpty());
    }

    @Test
    void lifecycle_methods_should_be_safe_but_not_strictly_tested() throws Exception {

        DataStorage storage = new DataStorage();

        DataReader reader = new DataReader() {

            @Override
            public void readData(DataStorage ds) {
                // no-op
            }

            @Override
            public void connect(DataStorage ds) {
                // intentionally minimal
            }

            @Override
            public void disconnect() {
                // nothing to clean up here yet
            }
        };

        // we mostly care that these don't explode during runtime
        reader.connect(storage);
        reader.disconnect();

        // no real assertions beyond stability
    }

}
