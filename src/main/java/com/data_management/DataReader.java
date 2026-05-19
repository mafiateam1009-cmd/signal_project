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

    
}
