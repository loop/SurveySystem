package connectivity;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is responsible for managing the socket connections that come into the server
 */
public class ServerConnections implements Runnable {

    private static final ArrayList<ClientConnection> connections = new ArrayList<>();
    private static final Logger log = Logger.getLogger(ServerConnections.class.getName());
    private final int port;
    private ServerSocket serverSocket;
    private boolean isServerListening;

    public ServerConnections(int port) {
        this.port = port;
    }

    /**
     * Starts the socket with the specified port and starts listening.
     */
    public void start() {
        ServerSocketFactory ssf = SSLServerSocketFactory.getDefault();
        try {
            try {
                ServerSocket ss = ssf.createServerSocket(this.port);
            } catch (IOException e) {
                e.printStackTrace();
            }
            this.isServerListening = true;

            if (isServerListening) {
                do {
                    ClientConnection clientConnection = new ClientConnection((SSLSocket) serverSocket.accept());
                    ServerConnections.addConnection(clientConnection);
                } while (isServerListening);
            }
        } catch (IOException e) {
            log.log(Level.FINE, e.getMessage());
        }
    }

    @Override
    public void run() {
        this.start();
    }

    /**
     * Stops listening and closes the socket.
     */
    public void stopListening() {
        this.isServerListening = false;
        try {
            if (this.serverSocket != null) {
                this.serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Keeps track of the connections.
     * @param clientConnection
     */
    public static void addConnection(ClientConnection clientConnection) {
        connections.add(clientConnection);
        clientConnection.start();

        log.log(Level.INFO, "A new connection with IP " + clientConnection.getSocket().getInetAddress());
    }

    /**
     * Closes a connection and removes it from the list of connections.
     *
     * @param incomingClientConnection process to be removed
     * @return true if closed else false
     * @throws IOException
     */
    public static boolean closeConnection(ClientConnection incomingClientConnection) throws IOException {
        boolean closed = false;
        if (!connections.isEmpty()) {
            for (Iterator<ClientConnection> iterator = connections.iterator(); iterator.hasNext(); ) {
                ClientConnection clientConnection = iterator.next();
                if (incomingClientConnection.hashCode() != clientConnection.hashCode()) {
                } else {
                    try {
                        incomingClientConnection.closeConnection();
                        connections.remove(clientConnection);
                        closed = true;
                        break;
                    } catch (IOException e) {
                        log.log(Level.SEVERE, "Error closing the socket connection");
                        e.printStackTrace();
                        throw e;
                    }
                }
            }
            if (!closed) {
                return false;
            }
            return true;
        }
        return false;
    }

    /**
     * Closes all socket connections.
     * @throws IOException
     */
    public static void closeAllConnections() throws IOException {
        for (Iterator<ClientConnection> iterator = connections.iterator(); iterator.hasNext(); ) {
            ClientConnection process = iterator.next();
            try {
                process.closeConnection();
            } catch (IOException e) {
                log.log(Level.SEVERE, "Error closing the socket connection");
                e.printStackTrace();
                throw e;
            }
        }
        connections.clear();
    }
}
