package com.yogeshn.fyp.androidclient.backend.connectivity;

import android.os.AsyncTask;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is reposible for requesting the server IP address over the network.
 *
 */

@SuppressWarnings("WeakerAccess")
public class NetworkThread extends AsyncTask {

    DatagramSocket datagramSocket;
    public static String ipAddress = "";

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            datagramSocket = new DatagramSocket();
            datagramSocket.setBroadcast(true);

            byte[] sendData = "request".getBytes();

            try {
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName("255.255.255.255"), 8888);
                datagramSocket.send(sendPacket);
                System.out.println(getClass().getName() + "Request packet sent to: 255.255.255.255");
            } catch (Exception ignored) {

            }

            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();

                if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                    continue;
                }

                for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast != null) {
                        try {
                            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, 8888);
                            datagramSocket.send(sendPacket);
                        } catch (Exception ignored) {

                        }
                        System.out.println(getClass().getName() + "Request packet sent to: " + broadcast.getHostAddress() + "; Intrerface: " + networkInterface.getDisplayName());
                    }
                }
            }
            byte[] recvBuff = new byte[15000];
            DatagramPacket recievePacket = new DatagramPacket(recvBuff, recvBuff.length);
            datagramSocket.receive(recievePacket);

            System.out.println(getClass().getName() + "Broadcast response from server: " + recievePacket.getAddress().getHostAddress());

            String message = new String(recievePacket.getData()).trim();
            if (message.equals("reply")) {
                ipAddress = recievePacket.getAddress().getHostName();
                System.out.println(recievePacket);
                System.out.println("IP" + ipAddress);
            }
            datagramSocket.close();
        } catch (IOException e) {
            Logger.getLogger(NetworkThread.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
}
