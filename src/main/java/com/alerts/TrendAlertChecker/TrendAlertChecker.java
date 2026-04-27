package com.alerts.TrendAlertChecker;

import java.util.List;

public interface TrendAlertChecker {

        public boolean checkTrend(List<Double> values, int windowSize, double threshold);

}
