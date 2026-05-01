package com.cardio_generator.outputs;

public interface OutputStrategy {
    /**
     * Defines a method for outputting health information for a specific patient. 
     * @param patientId the identifier of the patient
     * @param timestamp when the data was generated
     * @param label the type of data generated could be heart rate, blood pressure, etc.
     * @param data the actual data generated for the patient. 
     */
    void output(int patientId, long timestamp, String label, String data);
}
