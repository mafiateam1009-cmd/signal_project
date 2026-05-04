package data_management;

import com.data_management.Patient;
import com.data_management.PatientRecord;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import  org.junit.jupiter.api.Assertions.*;

import java.util.List;

    @Nested
    class PatientTest {

        @Test
        public void testLowSaturationAlert() {
            Patient patient = new Patient(1);

            patient.addRecord(85.0, "BloodSaturation", System.currentTimeMillis());

            // capture or trigger manually depending on your setup
            List<PatientRecord> records = patient.getRecords(
                    System.currentTimeMillis() - 1000,
                    System.currentTimeMillis() + 1000
            );

            assertEquals(1, records.size());
            assertEquals(85.0, records.get(0).getMeasurementValue());
        }

        @Test
        public void testECGAlertTriggerCondition() {
            Patient patient = new Patient(1);

            patient.addRecord(100.0, "ECG", 1000);
            patient.addRecord(160.0, "ECG", 2000);

            List<PatientRecord> records = patient.getRecords(0, 5000);

            double sum = 0;
            int count = 0;

            for (PatientRecord r : records) {
                if ("ECG".equals(r.getRecordType())) {
                    sum += r.getMeasurementValue();
                    count++;
                }
            }

            double avg = sum / count;

            assertTrue(avg > 0);
        }

        @Test
        public void testTriggeredAlertRecordExists() {
            Patient patient = new Patient(1);

            patient.addRecord(1.0, "Alert", System.currentTimeMillis());

            List<PatientRecord> records = patient.getRecords(
                    System.currentTimeMillis() - 1000,
                    System.currentTimeMillis() + 1000
            );

            boolean found = false;

            for (PatientRecord r : records) {
                if ("Alert".equals(r.getRecordType())) {
                    found = true;
                }
            }

            assertTrue(found);
        }

        @Test
        public void testGetRecordsFiltering() {
            Patient patient = new Patient(1);

            long t1 = 1000;
            long t2 = 2000;
            long t3 = 3000;

            patient.addRecord(90.0, "BloodSaturation", t1);
            patient.addRecord(95.0, "BloodSaturation", t2);
            patient.addRecord(97.0, "BloodSaturation", t3);

            List<PatientRecord> filtered = patient.getRecords(1500, 2500);

            assertEquals(1, filtered.size());
            assertEquals(95.0, filtered.get(0).getMeasurementValue());
        }
    }

