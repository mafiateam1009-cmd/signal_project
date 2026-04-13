package com.cardio_generator.outputs;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

/**
 * This class transfers data using the TCP connection.
 *   This class establishes the server which listens for the client to connect in order to transmit the data.
 */
public class TcpOutputStrategy implements OutputStrategy {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;

    /**
     * Constructor of the class, initializes the server which will listen to a certain port.
     * Uses an additional thread so the execution of the whole program won't be blocked waiting for a client.
     * @param port number to listen to
     */
    public TcpOutputStrategy(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("TCP Server started on port " + port);

            // Accept clients in a new thread to not block the main thread
            Executors.newSingleThreadExecutor().submit(() -> {
                try {
                    clientSocket = serverSocket.accept();
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    System.out.println("Client connected: " + clientSocket.getInetAddress());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *    Method which accepts data as input and transfers it via the socket.
     *         Result is encoded as a comma-separated string.
     *         @param patientId the identifier of the patient
     *         @param timestamp when it occurred
     *         @param label type of data
     *         @param data the data itself
     */
    @Override
    public void output(int patientId, long timestamp, String label, String data) {
        if (out != null) {
            // Put everything into a CSV format string
            String message = String.format("%d,%d,%s,%s", patientId, timestamp, label, data);
            out.println(message);
        }
    }
}
