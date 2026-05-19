package com.data_management;
import java.io.IOException;

public interface Connectable {
    /**
     * Establishes a connection to the data source. This method is responsible for
     * initializing any necessary resources and starting the data retrieval process.
     *
     * @throws IOException if there is an error during connection
     */
    void connect(DataStorage dataStorage) throws IOException;

    /**
     * Closes the connection to the data source and releases any resources that were
     * allocated during the connection.
     *
     * @throws IOException if there is an error during disconnection
     */
    void disconnect() throws IOException;
}