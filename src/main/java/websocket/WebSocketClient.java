package websocket;

import com.data_management.DataReader;
import com.data_management.DataStorage;
import com.data_management.Connectable;

import java.io.IOException;
import java.net.URI;
import javax.websocket.*;

/**
 * WebSocketClient establishes a connection with a WebSocket server and receives live patient informatio
 *
 * procedure:
 * 1. start by invoking connect(DataStorage) to establish the connection and initialize storage
 * 2. all incoming messages will be processed automatically via onMessage()
 * 3. every incoming message is parsed and saved to DataStorage
 * 4. end the session by calling disconnect()
 *
 * message format should be: "patientId,timestamp,label,value"
 * example: "1,1700000000,HeartRate,72.5"
 */

// honestly just keeping this simple for now, might refactor later if it gets even bigger
@ClientEndpoint
public class WebSocketClient implements DataReader, Connectable {

    private Session session; // or maybe "activeSession" would've been better... idk
    protected DataStorage dataStorage;

    /**
     * not supported for WebSocket
     * use connectt(DataStorage) instead for real-time input
     *
     * @throws UnsupportedOperationException always
     */

    @Override
    public void readData(DataStorage dataStorage) throws IOException {
        throw new UnsupportedOperationException("Use connect() for WebSocket real-time input");
    }

    @Override
    public void connect(DataStorage dataStorage) throws IOException {
        this.dataStorage = dataStorage;
        connect("ws://localhost:8080");
    }

    @Override
    public void disconnect() throws IOException {
        try {
            if (session != null && session.isOpen()) {
                session.close();
            }
        } catch (Exception e) {
            throw new IOException("Failed to disconnect", e);
        }
    }

    /**
     * establishes a WebSocket connection to the specified URI.
     * performs validation on the URI before connecting.
     *
     * @param uri the WebSocket server location such as "ws://localhost:8080"
     * @throws IOException if the URI is not valid or connection is unsuccessful
     */

    public void connect(String uri) throws IOException {

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();

        // quick guard - avoids confusing deeper websocket errors later
        if (uri == null || uri.isBlank()) {
            throw new IOException("WebSocket endpoint not configured");
        }

        final URI endpoint;
        try {
            endpoint = new URI(uri);
        } catch (Exception e) {
            // URI issues are considered configuration problems, not runtime failures
            throw new IOException("Invalid WebSocket endpoint: " + uri, e);
        }

        try {
            container.connectToServer(this, endpoint);

        } catch (Exception e) {
            // connection failures can happen due to network instability or server downtime
            throw new IOException("Unable to connect to WebSocket endpoint: " + uri, e);
        }
    }

    @OnOpen
    public void onOpen(Session s) {
        this.session = s;

        System.out.println("Connected to server");

        // just sanity check, sometimes I forget if it's actually connected lol
        if (session == null) {
            System.out.println("warning: session is null??");
        }
    }
    /**
     * automatically called upon receipt of a message from the server
     * this method parses the message and writes the data to DataStorage

     * format: "patientId,timestamp,label,value"
     * blank messages will be quietly discarded
     *
     * @param msg the message received from the server
     */
    @OnMessage
    public void onMessage(String msg) {

        System.out.println("Received: " + msg);

        if (msg == null || msg.isEmpty()) {
            return; // nothing to do here
        }

        // split assuming comma separated format... might break if format changes
        String[] parts = msg.split(",");

        int patientId  = Integer.parseInt(parts[0].trim());
        long timestamp = Long.parseLong(parts[1].trim());
        String label   = parts[2].trim();
        double value   = Double.parseDouble(parts[3].trim());
        dataStorage.addPatientData(patientId, value, label, timestamp);

        // debug leftover, sometimes helpful
        // System.out.println(parts.length);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {

        System.out.println("Connection closed: " + reason);

        // clearing reference just in case reconnect happens later
        this.session = null;

        // TODO maybe attempt reconnect? not sure yet
    }

    @OnError
    public void onError(Session session, Throwable t) {

        System.err.println("Error happened: " + t.getMessage());

        // not sure if logging stack trace is too noisy but leaving it
        t.printStackTrace();
    }

    /**
     * analyzes the message parts after splitting
     * this is done during the debugging process to verify the message format
     *
     * @param data the message divided by commas
     */

    private void process(String[] data) {

        // slight overkill maybe, but I like having a separate method here
        int len = data.length;

        System.out.println("Parsed length = " + len);

        // loop just to inspect values during debugging phase
        for (int i = 0; i < len; i++) {

            String val = data[i];

            if (val != null) {
                val = val.trim();
            } else {
                // didn't expect nulls but apparently it can happen?
                val = "";
            }

            // not doing anything with it yet, future me will handle it probably
            // System.out.println("val[" + i + "]=" + val);
        }

        // duplicate check logic (probably unnecessary but left it anyway)
        if (len == 0) {
            System.out.println("empty payload received");
        }
    }

}
