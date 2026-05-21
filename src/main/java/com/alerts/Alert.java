package com.alerts;

import com.alerts.Decorator.AlertI;

// Represents an alert
public class Alert implements AlertI {
    private String patientId;
    private String condition;
    private long timestamp;

    public Alert(String patientId, String condition, long timestamp) {
        this.patientId = patientId;
        this.condition = condition;
        this.timestamp = timestamp;
    }

    @Override
    public String getPatientId() {

        return patientId;
    }

    @Override
    public String getCondition() {
        return condition;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "Alert !!!" +
                "patientId='" + patientId + '\'' +
                ", condition='" + condition + '\'' +
                ", timestamp=" + timestamp +
                "\n";
    }
}
