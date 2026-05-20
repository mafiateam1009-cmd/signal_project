package com.alerts.Strategies;

import java.util.List;

import com.alerts.Alert;
import com.data_management.Patient;
import com.data_management.PatientRecord;

public class BloodPressureStrategy implements AlertStrategy {
        private String measurementLabel;

        public BloodPressureStrategy() {
            this.measurementLabel = "BloodPressure";
        }



        @Override
        public void checkAlert(Patient patient) {
            criticalBloodPressureThresholdCheck(patient);
            checkBloodPressureIncreasingTrend(patient, 5.0, 3); // Example: alert if average increase > 5 mmHg over 3 records
            checkBloodPressureDecreasingTrend(patient, 5.0, 3); // Example: alert if average decrease > 5 mmHg over 3 records
        }

        private void checkBloodPressureIncreasingTrend(Patient patient, double changeThreshold, int windowSize) {
            List<PatientRecord> records = patient.getRecordsbyType(this.measurementLabel);

            for(int i = 0; i <= records.size() - windowSize; i++) {
                boolean increasingTrend = false;
                double changeSum = 0;
                for(int j = 0; j < windowSize-1; j++) {
                    changeSum +=  records.get(i + j + 1).getMeasurementValue() - records.get(i + j).getMeasurementValue(); // positive change indicates increase
                }
                double meanChange = changeSum / (windowSize - 1);
                increasingTrend = meanChange > changeThreshold; 
                if(increasingTrend) {
                    triggerAlert(new Alert(
                            String.valueOf(patient.getPatientId()),
                            "Increasing Blood Pressure Trend",
                            records.get(i + windowSize - 1).getTimestamp()
                    ));
                }
            }

        }

        private void checkBloodPressureDecreasingTrend(Patient patient, double changeThreshold, int windowSize) {
            List<PatientRecord> records = patient.getRecordsbyType(this.measurementLabel);

            for(int i = 0; i <= records.size() - windowSize; i++) {
                boolean decreasingTrend = false;
                double changeSum = 0;
                for(int j = 0; j < windowSize-1; j++) {
                    changeSum += records.get(i + j + 1).getMeasurementValue() - records.get(i + j).getMeasurementValue(); // positive change indicates increase
                }
                double meanChange = changeSum / (windowSize - 1);
                decreasingTrend = meanChange < -changeThreshold; 
                if(decreasingTrend) {
                    triggerAlert(new Alert(
                            String.valueOf(patient.getPatientId()),
                            "Decreasing Blood Pressure Trend",
                            records.get(i + windowSize - 1).getTimestamp()
                    ));
                }
            }

        }

        /**
         * This methods triggers an alert if the systolic blood pressure exceeds 180mmHg or drops below 90mmHg,
         * or if the diastolic blood pressure exceeds 120mmHg or drops below 60mmHg. 
         * @param patient the patient whose blood pressure data is being evaluated for critical thresholds
         */
        private void criticalBloodPressureThresholdCheck(Patient patient) {
            List<PatientRecord> records = patient.getRecordsbyType(this.measurementLabel);

            for (PatientRecord record : records) {
                double bpValue = record.getMeasurementValue();
                if (bpValue > 180.0 || bpValue < 90.0) { // Systolic threshold
                    triggerAlert(new Alert(
                            String.valueOf(patient.getPatientId()),
                            "Critical Systolic Blood Pressure",
                            record.getTimestamp()
                    ));
                } else if (bpValue > 120.0 || bpValue < 60.0) { // Diastolic threshold
                    triggerAlert(new Alert(
                            String.valueOf(patient.getPatientId()),
                            "Critical Diastolic Blood Pressure",
                            record.getTimestamp()
                    ));
                }
            }

        }

        private void triggerAlert(Alert alert) {
            System.out.println("Alert triggered: " + alert.toString());
        }


    
}
