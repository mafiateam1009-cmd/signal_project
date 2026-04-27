package com.alerts.ThresholdChecker;

import java.util.List;

public class LowerThanThreshold implements ThresholdChecker {
    private final double threshold;

    public LowerThanThreshold(double threshold) {
        this.threshold = threshold;
    }

    @Override
    public boolean checkThreshold(List<Double> values) {
        for (Double value : values) {
            if (value >= threshold) {
                return false; // If any value is above or equal to the threshold, return false
            }
        }
        return true; // All values are below the threshold
    }
}