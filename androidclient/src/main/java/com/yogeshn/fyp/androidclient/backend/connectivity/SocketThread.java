package com.yogeshn.fyp.androidclient.backend.connectivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

/**
 * This class is responsible for openong a new socket and connecting to the server for every request
 * it recieved.
 *
 */
@SuppressWarnings({"FieldCanBeLocal", "CanBeFinal"})
public class SocketThread extends Thread {
    private static Socket socket;
    private final int PORT_NUMBER = 4000;
    private final String FINISH = "FINISH";
    private boolean connected;
    private String apimethod, serverIP, result;
    private CountDownLatch countDownLatch;

    /**
     * Public constructor for SocketThread.
     *
     * @param serverIP IP of a server to which we want to connect.
     * @param apimethod  Command we want to send to the server.
     * @param countDownLatch    Reference to a latch which we want to notify once the
     *                 response from server is received.
     */
    public SocketThread(String serverIP, String apimethod, CountDownLatch countDownLatch) {
        this.serverIP = serverIP;
        this.apimethod = apimethod;
        this.countDownLatch = countDownLatch;
        this.connected = false;
    }

    /**
     * Returns the result received from the server.
     *
     * @return The server's response.
     */
    public String getResult() {
        return this.result;
    }

    /**
     * Waits for a reply from the server and decrypts the result sent from server.
     */
    @Override
    public void run() {
        try {
            PrintWriter out = null;
            Socket socket = new Socket();
            InetAddress serverAddr = InetAddress.getByName(serverIP);
            socket.connect(new InetSocketAddress(serverAddr, PORT_NUMBER), 5000);
            connected = true;

            try {
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                String temporaryString = String.valueOf(TrafficEncryptor.encrypt(apimethod));
                temporaryString = temporaryString.replace("\n", "").replace("\r", "");
                temporaryString += "FINISH";
                out.println(temporaryString);

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String result = "", temp;

                if (!(temp = in.readLine()).endsWith("FINISH")) {
                    do {
                        result += temp;
                    } while (!(temp = in.readLine()).endsWith("FINISH"));
                }

                result += temp.substring(0, temp.length() - FINISH.length());

                this.result = TrafficEncryptor.decrypt(result);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
                socket.close();
                connected = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
            this.result = SocketAPI.SOCKET_TIMEOUT_EXCEPTION;
        } finally {
            if (socket == null || socket.isClosed()) {
            } else {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            connected = false;
            countDownLatch.countDown();
        }
    }
}