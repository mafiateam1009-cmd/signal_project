package com.alerts.Decorator;

public abstract class AlertDecorator implements AlertI {
    protected AlertI alert;

    public AlertDecorator(AlertI alert) {
        this.alert = alert;
    }

    @Override
    public String getPatientId() {
        return alert.getPatientId();
    }

    @Override
    public String getCondition() {
        return alert.getCondition();
    }

    @Override
    public long getTimestamp() {
        return alert.getTimestamp();
    }

    @Override
    public String toString() {
        return alert.toString();
    }
}
