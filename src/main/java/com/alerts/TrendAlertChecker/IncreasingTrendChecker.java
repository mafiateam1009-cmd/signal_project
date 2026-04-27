package com.alerts.TrendAlertChecker;

import java.util.List;

public class IncreasingTrendChecker implements TrendAlertChecker {

    /**
     * Checks for an increasing trend in a list of double values based on a specified window size and threshold.
     * It calculates the moving average for the specified window size and checks if there is a consistent
     * increase in the moving averages that exceeds the given threshold.
     * @param values The list of double values to analyze for trends
     * @param windowSize The number of recent values to consider for trend analysis 
     * @param threshold The minimum increase required to consider it an increasing trend
     * @return true if an increasing trend is detected, false otherwise
     *
     */
    @Override
    public boolean checkTrend(List<Double> values, int windowSize, double threshold) {
        // Implementation for checking increasing trend in a list of double values
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
                return false; // No increasing trend detected
            }
        }
        return true; // Increasing trend detected
    }
    
    public static void main(String []args) {
        // Example usage of the IncreasingTrendChecker
        List<Double> heartRateValues = List.of(80.0, 85.0, 90.0, 95.0, 100.0);
        IncreasingTrendChecker trendChecker = new IncreasingTrendChecker();
        boolean isIncreasing = trendChecker.checkTrend(heartRateValues, 3, 5.0);
        System.out.println("Is there an increasing trend? " + isIncreasing);
    }
}