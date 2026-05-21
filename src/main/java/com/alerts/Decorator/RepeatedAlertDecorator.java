package com.alerts.Decorator;

public class RepeatedAlertDecorator extends AlertDecorator {

    int repeatCount;

    public RepeatedAlertDecorator(AlertI alert, int repeatCount) {
        super(alert);
        this.repeatCount = repeatCount;
    }

    public int getRepeatCount() {
        return repeatCount;
    }

    @Override
    public String toString() {
        return (alert.toString() + "\n").repeat(repeatCount);
    }
}
