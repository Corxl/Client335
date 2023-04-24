package me.corxl.client335;

import me.corxl.client335.user.UserInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Client extends Thread {
    private Socket socket = null;
    private ObjectInputStream input = null;
    private ObjectOutputStream ioout = null;
    private final LoginWindow WINDOW;
    private String ip;
    private final int DEFAULT_PORT; // 4909
    private final int TIMEOUT = 5000;

    public Client(LoginWindow window, String ip, int port) {
        this.WINDOW = window;
        this.ip = ip;
        this.DEFAULT_PORT = port;
    }

    @Override
    public void run() {
        try {
            WINDOW.toggleConnecting(true, true);
            socket = new Socket();
            socket.connect(new InetSocketAddress(ip, DEFAULT_PORT), TIMEOUT);
            WINDOW.toggleConnecting(false, false);
            WINDOW.toggleConnect();
            WINDOW.toggleLogin();
        } catch (IOException | InterruptedException e) { // disconnected from server or error.
            WINDOW.toggleCannotConnect(true);
        }
    }

    public void disconnect() throws IOException {
        if (this.socket != null)
            try {
                this.socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        if (this.input != null)
            try {
                this.input.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        if (this.ioout != null)
            try {
                this.ioout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        WINDOW.toggleLoginOutline(false);
        WINDOW.clearLoginInputs();
        WINDOW.hideNotificationBox();
    }

    public void logout() {
        sendData(new Object[]{"logout"});
        WINDOW.hideNotificationBox();
    }

    private void sendData(Object[] data) {
        try {
            ObjectOutputStream obOut = new ObjectOutputStream(this.socket.getOutputStream());
            obOut.writeObject(data);
            obOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object receiveData() throws IOException, ClassNotFoundException {
        ObjectInputStream obIn = new ObjectInputStream(this.socket.getInputStream());
        Object read = obIn.readObject();
        return read;
    }

    public void requestLogin(String usernameOrEmail, String password) {
        try {
            Object[] data = new Object[]{"login", usernameOrEmail, password};
            sendData(data);

            Object[] result = (Object[]) receiveData();
            String resultType = (String) result[0];
            if (resultType.equals("loginSuccess")) {
                String resultEmail = ((String) result[1]);
                String resultUsername = ((String) result[2]);
                String resultTimeOfReg = ((String) result[3]);
                UserInfo info = new UserInfo(resultEmail, resultUsername, resultTimeOfReg);
                //System.out.println("Email: " + info.getEmail() + "\nUsername: " + info.getUsername() + "\nTime of registration: " + info.getTimeOfRegistration());
                WINDOW.showUserInfo(info.getEmail(), info.getUsername(), info.getTimeOfRegistration());
                WINDOW.toggleLogin(true, true);
                WINDOW.clearLoginInputs();
                WINDOW.toggleLoginOutline(false);
            } else {
                // logic for error visuals
                WINDOW.toggleLoginOutline(true);
                //
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public void requestRegister(String email, String username, String password, String confirmPassword) {
        this.sendData(new Object[]{"register", email, username, password, confirmPassword});
        try {
            Object[] data = (Object[]) this.receiveData();
            String type = (String) data[0];
            if (type.equals("registerFailed")) {
                String emailVerification = (String) data[1];
                String usernameVerification = (String) data[2];
                String passwordVerification = (String) data[3];
                String confirmPasswordVerification = (String) data[4];
                this.WINDOW.regPageErrors(new String[]{emailVerification, usernameVerification, passwordVerification, confirmPasswordVerification}, true);
            } else if (type.equals("registerSuccess")) {
                this.WINDOW.toggleRegisterSuccess(true);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
