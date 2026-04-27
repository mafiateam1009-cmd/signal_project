package com.alerts.ThresholdChecker;

import java.util.List;

public class BetweenThreshold implements ThresholdChecker {
    private final double lowerBound;
    private final double upperBound;

    public BetweenThreshold(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    @Override
    public boolean checkThreshold(List<Double> values) {
        for (double value : values) {
            if (value < lowerBound || value > upperBound) {
                return false; // Value is outside the specified range
            }
        }
        return true; // All values are within the specified range
    }
}