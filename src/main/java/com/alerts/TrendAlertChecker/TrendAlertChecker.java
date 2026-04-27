package com.alerts.TrendAlertChecker;

import com.data_management.Patient;

public interface TrendAlertChecker {

    boolean checkTrend(Patient patient, String recordType, int windowSize, 
                       double threshold, long startTime, long endTime);
}
