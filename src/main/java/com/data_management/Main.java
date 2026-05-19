package com.data_management;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.cardio_generator.HealthDataSimulator;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length > 0 && args[0].equals("DataStorage")) {
            Path defaultPath = Paths.get("src", "main", "java", "com", "data_management", "health_data.csv");
            String dataFilePath = args.length > 1 ? args[1] : defaultPath.toString();
            System.out.println("Loading data from: " + dataFilePath);
            DataReader fileReader = new FileDataReader(dataFilePath);
            DataStorage dataStorage = new DataStorage(fileReader);
            // Example of retrieving patient data
            int examplePatientId = 1; // Replace with actual patient ID to retrieve
            long startTime = 1700000000000L; // Replace with actual start time
            long endTime = 1800000000000L; // Replace with actual end time
            System.out.println("Retrieving records for patient ID: " + examplePatientId);
            for (PatientRecord record : dataStorage.getRecords(examplePatientId, startTime, endTime)) {
                System.out.println(record);
            }
        } else {
            HealthDataSimulator.main(new String[]{});
        }
    }
}
