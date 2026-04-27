package com.alerts.ThresholdChecker;

import java.util.List;

public interface ThresholdChecker {
    boolean checkThreshold(List<Double> values);
}