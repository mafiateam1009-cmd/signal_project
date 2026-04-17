package com.cardio_generator.generators;

import com.cardio_generator.outputs.OutputStrategy;

public interface PatientDataGenerator {
    /**
     * Generates health data for a specific patient and outputs it using the provided output strategy. 
     * @param patientId the unique identifier of the patient for whom the data is being generated
     * @param outputStrategy the strategy used to send the data. This could be the console, file, websocket or TCP socket output.
     */
    void generate(int patientId, OutputStrategy outputStrategy);
}
