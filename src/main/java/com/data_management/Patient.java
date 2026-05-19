package com.data_management;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a patient and manages their medical records.
 * This class stores patient-specific data, allowing for the addition and
 * retrieval
 * of medical records based on specified criteria.
 */
public class Patient {
    private int patientId;
    private List<PatientRecord> patientRecords;

    /**
     * Constructs a new Patient with a specified ID.
     * Initializes an empty list of patient records.
     *
     * @param patientId the unique identifier for the patient
     */
    public Patient(int patientId) {
        this.patientId = patientId;
        this.patientRecords = new ArrayList<>();
    }

    /**
     * Adds a new record to this patient's list of medical records.
     * The record is created with the specified measurement value, record type, and
     * timestamp.
     *
     * @param measurementValue the measurement value to store in the record
     * @param recordType       the type of record, e.g., "HeartRate",
     *                         "BloodPressure"
     * @param timestamp        the time at which the measurement was taken, in
     *                         milliseconds since UNIX epoch
     */
    public void addRecord(double measurementValue, String recordType, long timestamp) {
        PatientRecord record = new PatientRecord(this.patientId, measurementValue, recordType, timestamp);
        this.patientRecords.add(record);
    }

    /**
     * Retrieves a list of PatientRecord objects for this patient that fall within a
     * specified time range.
     * The method filters records based on the start and end times provided.
     *
     * @param startTime the start of the time range, in milliseconds since UNIX
     *                  epoch
     * @param endTime   the end of the time range, in milliseconds since UNIX epoch
     * @return a list of PatientRecord objects that fall within the specified time
     *         range
     */
    public List<PatientRecord> getRecords(long startTime, long endTime) {
        /**
         * Loops through all the patients saved in the system and filters those that
         * have the timestamps within the given range. A record is considered valid if its
         * timestamp is greater than or equal to the start time and less than or equal to the
         * end time. This function returns a collection of filtered records.
         */
        List<PatientRecord> filteredRecords = new ArrayList<>();
        for (PatientRecord record : this.patientRecords) {
            if (record.getTimestamp() >= startTime && record.getTimestamp() <= endTime) {
                filteredRecords.add(record);
            }
        }
        return filteredRecords;
    }

    public List<PatientRecord> getRecordsbyType(String recordType) {
        /**
         * Loops through all the patients saved in the system and filters those that
         * have the record type matching the given type. A record is considered valid if its
         * type is equal to the given type. This function returns a collection of filtered records.
         */
        List<PatientRecord> filteredRecords = new ArrayList<>();
        for (PatientRecord record : this.patientRecords) {
            if (record.getRecordType().equals(recordType)) {
                filteredRecords.add(record);
            }
        }
        return filteredRecords;
    }

    //added this here to make my life easier in AlertGenerator
    public List<PatientRecord> getRecords(){
        return patientRecords;
    }
}
