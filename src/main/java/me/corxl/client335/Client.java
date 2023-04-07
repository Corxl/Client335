package me.corxl.client335;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client extends Thread {
    private Socket socket = null;
    private ObjectInputStream input = null;
    private ObjectOutputStream ioout = null;
    private LoginWindow window;
    private String ip;
    private final int port; // 4909

    public Client(LoginWindow window, String ip, int port) {
        this.window = window;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try {

            socket = new Socket(ip, port);
            System.out.println("Connected to server.");
            window.toggleConnect();
            window.toggleLogin();
            while (true) {

            }
        } catch (IOException e) { // disconnected from server or error.
            throw new RuntimeException(e);
            // window.connectionError()

        }
    }

}
