package com.alerts.Decorator;

public class PriorityAlertDecorator extends AlertDecorator {

    String priorityLabel;

    public PriorityAlertDecorator(AlertI alert) {
        super(alert);
    }

    public PriorityAlertDecorator(AlertI alert, String priorityLabel) {
        super(alert);
        this.priorityLabel = priorityLabel;
    }

    public String getPriorityLabel() {
        return priorityLabel;
    }

    public void setPriorityLabel(String priorityLabel) {
        this.priorityLabel = priorityLabel;
    }

    @Override
    public String toString() {
        return "Priority: " + priorityLabel + " " + alert.toString();
    }
}
