package com.alerts.ThresholdChecker;

import java.util.List;

public class GreaterThanThreshold implements ThresholdChecker {
    private final double threshold;

    public GreaterThanThreshold(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean checkThreshold(List<Double> values) {
        for (Double value : values) {
            if (value > threshold) {
                return true; // Alert condition met
            }
        }
        return false; // No values exceeded the threshold
    }
}