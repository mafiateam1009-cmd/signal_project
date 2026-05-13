package com.data_management;

import java.io.IOException;

public interface DataReader {
    /**
     * reads data from a given source and places it into the data storage.
     *       used for batch/file reading.
     *
     *       @param dataStorage the data storage
     *       @throws IOException if there is any problem with reading the data
     */
    void readData(DataStorage dataStorage) throws IOException;

    /**
     * Connects to the source of live data feeds and begins retrieving data from it.
     *
     * @param dataStorage the repository where the retrieved data is to be stored
     * @throws IOException when connection to the source fails
     */
    void connect(DataStorage dataStorage) throws IOException;

    /**
     * disconnects from the real-time data source and stops receiving data.
     *
     * @throws IOException if there is an error during disconnection
     */
    void disconnect() throws IOException;
}
