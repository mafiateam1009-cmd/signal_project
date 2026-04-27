package com.alerts.TrendAlertChecker;

import java.util.List;


public class DecreasingTrendChecker implements TrendAlertChecker {

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
    public boolean checkTrend(List<Double> values, int windowSize, double threshold) {
        if (values.size() < windowSize) {
            return false; // Not enough data to analyze
        }

        // Calculate the moving average for the specified window size
        double[] movingAverages = new double[values.size() - windowSize + 1];
        for (int i = 0; i <= values.size() - windowSize; i++) {
            double sum = 0;
            for (int j = 0; j < windowSize; j++) {
                sum += values.get(i + j);
            }
            movingAverages[i] = sum / windowSize;
        }

        // Check for an increasing trend in the moving averages
        for (int i = 1; i < movingAverages.length; i++) {
            if (movingAverages[i] - movingAverages[i - 1] < threshold) {
                return true; // No decreasing trend detected
            }
        }

        return false; // Decreasing trend detected
    }
    
    public static void main(String []args) {
        // Example usage of the DecreasingTrendChecker
        List<Double> heartRateValues = List.of(100.0, 95.0, 90.0, 85.0, 80.0);
        DecreasingTrendChecker trendChecker = new DecreasingTrendChecker();
        boolean isDecreasing = trendChecker.checkTrend(heartRateValues, 3, 5.0);
        System.out.println("Is there a decreasing trend? " + isDecreasing);
    }
}
