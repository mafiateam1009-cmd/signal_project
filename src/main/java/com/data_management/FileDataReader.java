package com.data_management;

import java.io.BufferedReader;
import java.io.IOException;

public class FileDataReader implements DataReader {
    /**
     * This class is capable of readng data from an output file generated using the --output file: <output_dir> argument. 
     */
    
    String filePath;
    public FileDataReader(String filePath) {
        this.filePath = filePath;
    }
    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) { 
                    try {
                        int patientId = Integer.parseInt(parts[0].trim());
                        double measurementValue = Double.parseDouble(parts[1].trim());
                        String recordType = parts[2].trim();
                        long timestamp = Long.parseLong(parts[3].trim());
                        dataStorage.addPatientData(patientId, measurementValue, recordType, timestamp);
                    } catch (NumberFormatException e) {
                        System.err.println("Skipping invalid line: " + line);
                    }
                } else {
                    if (!line.trim().isEmpty()) {
                        System.err.println("Skipping malformed line: " + line);
                    }
                }
            }
        } catch (IOException e) {
            throw new IOException("Could not read data file: " + filePath + " (" + e.getMessage() + ")", e);
        }
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("No file path provided. Please provide the pah to the data file as an argument.");
            return;
        }
        String filePath = args[0];
        DataStorage dataStorage = new DataStorage(new FileDataReader(filePath));
        // Now dataStorage is populated with the data from the file and can be used for further processing.
    }
    


}
