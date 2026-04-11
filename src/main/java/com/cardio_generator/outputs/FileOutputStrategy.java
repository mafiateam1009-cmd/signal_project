package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ConcurrentHashMap;

public class FileOutputStrategy implements OutputStrategy {

    private String BaseDirectory;

    public final ConcurrentHashMap<String, String> file_map = new ConcurrentHashMap<>();

    /**
     * Constructor for FileOutputStrategy.
     * @param baseDirectory The base directory where the output files will be stored. Each label will have its own file named <label>.txt in this directory.
     */
    public FileOutputStrategy(String baseDirectory) { // Class name was written in lowerCamelCase instead of Upper Camel Case recommended by section 5.2.2 of the google java style guide. Changed the classname to UpperCamelCase.

        this.BaseDirectory = baseDirectory;
    }

    @Override
    /**
     * Outputs the data to a file named <label>.txt in the base directory. If the file does not exist, it will be created. If it already exists, the new data will be appended to the file.
     * @param patientId The ID of the patient.
     * @param timestamp The timestamp of the data.
     * @param label The label for the data.
     * @param data The data to be output.
     * @throws IOException If an I/O error occurs while creating the directory or writing to the file.
     */
    public void output(int patientId, long timestamp, String label, String data) {
        try {
            // Create the directory
            Files.createDirectories(Paths.get(BaseDirectory));
        } catch (IOException e) {
            System.err.println("Error creating base directory: " + e.getMessage());
            return;
        }
        // Set the FilePath variable
        String FilePath = file_map.computeIfAbsent(label, k -> Paths.get(BaseDirectory, label + ".txt").toString());

        // Write the data to the file
        try (PrintWriter out = new PrintWriter(
                Files.newBufferedWriter(Paths.get(FilePath), StandardOpenOption.CREATE, StandardOpenOption.APPEND))) {
            out.printf("Patient ID: %d, Timestamp: %d, Label: %s, Data: %s%n", patientId, timestamp, label, data);
        } catch (Exception e) {
            System.err.println("Error writing to file " + FilePath + ": " + e.getMessage());
        }
    }
}