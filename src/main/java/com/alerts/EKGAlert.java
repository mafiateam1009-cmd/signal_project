package com.alerts;

public class EKGAlert extends Alert {

    public EKGAlert(String patientId, String condition, long timestamp) {
        super(patientId, condition, timestamp);
    }
}
