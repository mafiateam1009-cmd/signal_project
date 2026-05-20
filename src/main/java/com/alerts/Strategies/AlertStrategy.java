package com.alerts.Strategies;

import java.util.List;

import com.data_management.Patient;
import com.data_management.PatientRecord;

public interface AlertStrategy {
    

    public void checkAlert(Patient patient);

}
