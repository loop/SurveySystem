package connectivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is responsible for broadcasting the server IP address in a UDP packet to the client.
 */

public class DiscoveryThread implements Runnable {

    private static final Logger log = Logger.getLogger(DiscoveryThread.class.getName());
    DatagramSocket socket;
    private static volatile boolean running = true;

    @Override
    public void run() {
        while (running) {
            try {
                socket = new DatagramSocket(8888, InetAddress.getByName("0.0.0.0"));
                socket.setBroadcast(true);

                while (true) {
                    log.log(Level.INFO, "Ready to receive broadcast packets");

                    byte[] bytes = new byte[15000];
                    DatagramPacket datagramPacket = new DatagramPacket(bytes, bytes.length);
                    socket.receive(datagramPacket);

                    log.log(Level.INFO, "Discovery packet received from: " + datagramPacket.getAddress().getHostAddress());
                    log.log(Level.INFO, "Packet received; data: " + new String(datagramPacket.getData()));

                    String message = new String(datagramPacket.getData()).trim();
                    switch (message) {
                        case "request":
                            byte[] sendData = "reply".getBytes();

                            DatagramPacket clientPacket = new DatagramPacket(sendData, sendData.length, datagramPacket.getAddress(), datagramPacket.getPort());
                            socket.send(clientPacket);

                            log.log(Level.INFO, "Sent packet to: " + clientPacket.getAddress().getHostAddress());
                            break;
                    }
                }
            } catch (IOException e) {
                Logger.getLogger(DiscoveryThread.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    public static DiscoveryThread getInstance() {
        return DiscoveryThreadHolder.INSTANCE;
    }

    private static class DiscoveryThreadHolder {
        private static final DiscoveryThread INSTANCE = new DiscoveryThread();
    }

    public static void terminate() {
        running = false;
    }
}
