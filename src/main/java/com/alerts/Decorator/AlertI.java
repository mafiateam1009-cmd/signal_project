package com.alerts.Decorator;

public interface AlertI {
    String getPatientId();
    String getCondition();
    long getTimestamp();
    @Override
    String toString();
}
