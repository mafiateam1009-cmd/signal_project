package com.data_management;

import java.io.IOException;
import websocket.WebSocketClient;

public class SocketDataReader implements DataReader {

    /**
     * Handle real-time data from a WebSocket server instead of reading from a static file.
     */
    private String uri;

    public SocketDataReader(String uri) {
        this.uri = uri;
    }

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        WebSocketClient client = new WebSocketClient();
        if (this.uri != null && !this.uri.isBlank()) {
            client.connect(this.uri, dataStorage);
        } else {
            client.connect(dataStorage);
        }

        // keep the client connection alive until the application shuts down
        // real-time streams are continuous, so we don't close it here
    }
}
