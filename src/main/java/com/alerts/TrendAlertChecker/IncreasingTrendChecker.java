package com.alerts.TrendAlertChecker;

import java.util.List;

import com.data_management.Patient;
import com.data_management.PatientRecord;

public class IncreasingTrendChecker implements TrendAlertChecker {

    /** This method checks for an increasing trend in the specified record type for a patient over a given window size and threshold.
     * It should analyze the patient's data records, identify the relevant records of the specified type, and determine if there using a moving average to analyze trends over time in the data..
     * 
     * 
     * @param patient The patient whose data is being analyzed
     * @param recordType The type of record to check for trends (e.g., "heart_rate", "blood_pressure")
     * @param windowSize The number of recent records to consider for trend analysis
     * @param threshold The minimum increase required to consider it an increasing trend
     * @return true if an increasing trend is detected, false otherwise
     */
    @Override
    public boolean checkTrend(Patient patient, String recordType, int windowSize, double threshold, long startTime, long endTime) {
        List<PatientRecord> records = patient.getRecordsByType(recordType, startTime, endTime);
        
        if (records.size() < windowSize) {
            return false; // Not enough data to analyze
        }

        // Calculate the moving average for the specified window size
        double[] movingAverages = new double[records.size() - windowSize + 1];
        for (int i = 0; i <= records.size() - windowSize; i++) {
            double sum = 0;
            for (int j = 0; j < windowSize; j++) {
                sum += records.get(i + j).getMeasurementValue();
            }
            movingAverages[i] = sum / windowSize;
        }

        // Check for an increasing trend in the moving averages
        for (int i = 1; i < movingAverages.length; i++) {
            if (movingAverages[i] - movingAverages[i - 1] < threshold) {
                return false; // No increasing trend detected
            }
        }

        return true; // Increasing trend detected
    }
}
