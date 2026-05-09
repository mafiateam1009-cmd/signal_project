package com.alerts;

import java.util.List;

import com.data_management.DataStorage;
import com.data_management.Patient;
import com.data_management.PatientRecord;

/**
 * The {@code AlertGenerator} class is responsible for monitoring patient data
 * and generating alerts when certain predefined conditions are met. This class
 * relies on a {@link DataStorage} instance to access patient data and evaluate
 * it against specific health criteria.
 */
public class AlertGenerator {
    private DataStorage dataStorage;

    /**
     * Constructs an {@code AlertGenerator} with a specified {@code DataStorage}.
     * The {@code DataStorage} is used to retrieve patient data that this class
     * will monitor and evaluate.
     *
     * @param dataStorage the data storage system that provides access to patient
     *                    data
     */
    public AlertGenerator(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    /**
     * Evaluates the specified patient's data to determine if any alert conditions
     * are met. If a condition is met, an alert is triggered via the
     * {@link #triggerAlert}
     * method. This method should define the specific conditions under which an
     * alert
     * will be triggered.
     *
     * @param patient the patient data to evaluate for alert conditions
     */
    public void evaluateData(Patient patient) {
        checkLowSaturation(patient);
        checkRapidDrop(patient);
        checkECG(patient);
        checkTriggeredAlert(patient);
    }
    private void checkLowSaturation(Patient patient) {

        for (PatientRecord record : patient.getRecords()) {

            if (!"BloodSaturation".equals(record.getRecordType())) continue;

            double sat = record.getMeasurementValue();

            // clinic guideline: alert below 92 unless already flagged upstream
            if (sat < 92.0) {

                triggerAlert(new Alert(
                        String.valueOf(patient.getPatientId()),
                        "Low Blood Saturation",
                        record.getTimestamp()
                ));
            }
        }

    }

    private void checkRapidDrop(Patient patient) {

        var records = patient.getRecords();

// NOTE: assumes sorted by ingestion time (not strictly validated here)
        for (int i = 1; i < records.size(); i++) {

            var prev = records.get(i - 1);
            var curr = records.get(i);

            if (!"BloodSaturation".equals(prev.getRecordType())) continue;
            if (!"BloodSaturation".equals(curr.getRecordType())) continue;

            double delta = prev.getMeasurementValue() - curr.getMeasurementValue();
            long window = curr.getTimestamp() - prev.getTimestamp();

            // 5% drop within 10min window (threshold agreed with cardio team)
            if (delta >= 5.0 && window <= 600_000) {

                triggerAlert(new Alert(
                        String.valueOf(patient.getPatientId()),
                        "Rapid Saturation Drop",
                        curr.getTimestamp()
                ));
            }
        }

    }

    /**
     * Checks for an increasing trend in blood pressure values for the given patient. If an increasing trend is detected based on the specified change threshold and window size, an alert is triggered.
     * @param patient the patient whose blood pressure data is being evaluated
     * @param changeThreshold the minimum average change in blood pressure values over the specified window size that would indicate an increasing trend and trigger an alert
     * @param windowSize the number of consecutive records to consider when evaluating the trend
     */
    public void checkBloodPressureIncreasingTrend(Patient patient, double changeThreshold, int windowSize) {
        List<PatientRecord> records = patient.getRecordsbyType("BloodPressure");

        for(int i = 0; i < records.size() - windowSize; i++) {
            boolean increasingTrend = false;
            double changeSum = 0;
            for(int j = 0; j < windowSize-1; j++) {
                changeSum += Math.abs(records.get(i + j).getMeasurementValue() - records.get(i + j + 1).getMeasurementValue());
            }
            double meanChange = changeSum / (windowSize - 1);
            increasingTrend = meanChange > changeThreshold; // Example condition for increasing trend, can be adjusted based on requirements
            if(increasingTrend) {
                triggerAlert(new Alert(
                        String.valueOf(patient.getPatientId()),
                        "Increasing Blood Pressure Trend",
                        records.get(i + windowSize - 1).getTimestamp()
                ));
            }
        }

    }
    private void checkECG(Patient patient) {

        var records = patient.getRecords();

        double sum = 0;
        int n = 0;

        for (var r : records) {
            if ("ECG".equals(r.getRecordType())) {
                sum += r.getMeasurementValue();
                n++;
            }
        }

        if (n == 0) return;

        double avg = sum / n;

//spike detection against current patient baseline (very naive baseline)
        for (var r : records) {

            if (!"ECG".equals(r.getRecordType())) continue;

            double v = r.getMeasurementValue();

            if (v > avg + 50.0) {

                triggerAlert(new Alert(
                        String.valueOf(patient.getPatientId()),
                        "Abnormal ECG Peak",
                        r.getTimestamp()
                ));
            }
        }

    }

    private void checkTriggeredAlert(Patient patient) {

        for (var r : patient.getRecords()) {

            if (!"Alert".equals(r.getRecordType())) continue;

            // legacy encoding: 1.0 = manual trigger event
            if (r.getMeasurementValue() == 1.0) {

                triggerAlert(new Alert(
                        String.valueOf(patient.getPatientId()),
                        "Manual Alert Triggered",
                        r.getTimestamp()
                ));
            }
        }

    }



    /**
     * Triggers an alert for the monitoring system. This method can be extended to
     * notify medical staff, log the alert, or perform other actions. The method
     * currently assumes that the alert information is fully formed when passed as
     * an argument.
     *
     * @param alert the alert object containing details about the alert condition
     */
    private void triggerAlert(Alert alert) {
        System.out.println("ALERT: " + alert);

    }
}
