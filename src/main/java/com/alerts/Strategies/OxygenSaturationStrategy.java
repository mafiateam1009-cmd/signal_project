package com.alerts.Strategies;

import com.alerts.Alert;
import com.data_management.Patient;

public class OxygenSaturationStrategy implements AlertStrategy {
    private String measurementLabel;

    public OxygenSaturationStrategy() {
        this.measurementLabel = "BloodSaturation";
    }

    @Override
    public void checkAlert(Patient patient) {
        checkRapidDrop(patient);
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

    private void triggerAlert(Alert alert) {
        System.out.println("ALERT: " + alert);
    }

}