package connectivity;

import javax.net.ssl.SSLSocket;
import java.net.*;
import java.io.*;

/**
 * This class is responsible for maintaining the connections coming into the server
 * which is given a separate SocketProcess thread which handles the requests.
 */
public class ClientConnection extends Thread {

    private final Socket socket;
    String inputLine, outputLine, message;


    public ClientConnection(SSLSocket socket) {
        this.socket = socket;
    }

    /**
     * Runs the thread and checks for incoming messages and sends outgoing messages.
     */
    public void run() {
        try (
                PrintWriter out = new PrintWriter(this.socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()))
            ) {
            label:
            do {
                if (!(inputLine = in.readLine()).endsWith("END")) {
                    do {
                    } while (!(inputLine = in.readLine()).endsWith("END"));
                }

                message = inputLine.substring(0, inputLine.length() - "END".length());

                outputLine = SocketAPI.getResponseFor(message);
                out.println(outputLine);

                switch (outputLine) {
                    case "Close":
                        break label;
                }
            } while (true);
            ServerConnections.closeConnection(this);
        } catch (SocketException e) {
            System.out.println("Closed socket " + socket.getInetAddress());
        } catch (NullPointerException e) {
            try {
                ServerConnections.closeConnection(this);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the socket connection
     *
     * @throws IOException
     */
    public void closeConnection() throws IOException {
        try {
            this.socket.close();
        } catch (IOException e) {
            System.err.println("Error closing connection");
            e.printStackTrace();
            throw e;
        }
    }

    public Socket getSocket() {
        return socket;
    }
}