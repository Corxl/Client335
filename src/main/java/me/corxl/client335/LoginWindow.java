package me.corxl.client335;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

public class LoginWindow implements Initializable {
    @FXML
    private Button cncGoBack;

    @FXML
    private VBox connectInfoBox;

    @FXML
    private Label connectingLabel;

    @FXML
    private VBox connnecting;

    @FXML
    private VBox couldNotConnect;

    @FXML
    private Button disconnect;

    @FXML
    private VBox forgotPasswordBox;

    @FXML
    private TextField forgotPasswordEmail;

    @FXML
    private Button goToLogin;

    @FXML
    private VBox infoDisplay;

    @FXML
    private TextField ipaddressInput;

    @FXML
    private VBox loginInfoBox;

    @FXML
    private Button logoutButton;

    @FXML
    private TextArea notificationBox;

    @FXML
    private PasswordField passwordInput;

    @FXML
    private TextField portInput;

    @FXML
    private Label regInvalidEmail;

    @FXML
    private Label regInvalidPasswordFormat;

    @FXML
    private Label regInvalidUsername;

    @FXML
    private Label regMatchPassword;

    @FXML
    private PasswordField registerConfirmPassword;

    @FXML
    private TextField registerEmail;

    @FXML
    private VBox registerInfoBox;

    @FXML
    private PasswordField registerPassword;

    @FXML
    private VBox registerSuccess;

    @FXML
    private TextField registerUsername;

    @FXML
    private HBox top;

    @FXML
    private TextField usernameInput;
    @FXML
    private Label forgotPasswordText;
    @FXML
    private Label forgotPasswordP;
    @FXML
    private PasswordField forgotPasswordNewPassword;
    private double xOffset = 0;
    private double yOffset = 0;
    private Stage stage;
    private boolean showingPassword = true;
    private String recentPassword = "";
    private boolean loginLine = false;
    private boolean showRegisterPage = false;
    private Client client;
    private Thread connectingVisual;

    @FXML
    void loginClick(MouseEvent event) {
        client.requestLogin(this.usernameInput.getText(), this.passwordInput.getText());
    }
    @FXML
    void onExitClick(MouseEvent event) {
        stage.hide();
        System.exit(0);
    }
    @FXML
    void onMinimize(MouseEvent event) {
        stage.setIconified(true);
    }
    @FXML
    void registerClick(ActionEvent event) {
        toggleRegister(showRegisterPage);
    }
    @FXML
    void connectClick(ActionEvent event) throws UnknownHostException {
        //System.out.println(InetAddress.getLocalHost().getHostAddress());
        try {
            // Working w inputs
            //this.client = new Client(this, ipaddressInput.getText().trim(), Integer.parseInt(portInput.getText().trim()));

            // Debug input
            this.client = new Client(this, InetAddress.getLocalHost().getHostAddress(), 4909);
            this.client.start();
        } catch (NumberFormatException e) {
            // port is not an integer
        }

    }
    @FXML
    void logoutClick(ActionEvent event) {
        toggleLogin(false, false);
        this.client.logout();
    }
    @FXML
    void disconnect(ActionEvent event) throws IOException {
        toggleConnect(false, false);
        toggleLogin(true, false);
        toggleRegister(true);
        this.client.disconnect();
        this.hideForgotPassword();
    }
    @FXML
    void passwordInputTyped(KeyEvent event) {
        toggleOutline(false, passwordInput);
        if (!this.showingPassword) {
            this.recentPassword = this.passwordInput.getPromptText() + this.passwordInput.getText();
            this.passwordInput.setPromptText(recentPassword);
            this.passwordInput.setText("");
        }
    }
    @FXML
    void usernameInputTyped(KeyEvent event) {
        toggleOutline(false, usernameInput);
    }
    @FXML
    void connectGoBack(ActionEvent event) {
        this.blur(connectInfoBox, false);
        this.couldNotConnect.setVisible(false);
    }
    @FXML
    void passChanged(KeyEvent event) {
        if (!this.showingPassword) {
            this.recentPassword = this.passwordInput.getPromptText() + this.passwordInput.getText();
            this.passwordInput.setPromptText(recentPassword);
            this.passwordInput.setText("");
        }

    }
    @FXML
    void showPassword(MouseEvent event) {
        showPassword(this.showingPassword);
        showingPassword = !showingPassword;
    }
    @FXML
    void forgotPasswordClick(MouseEvent event) {
        showForgotPassword();
    }
    @FXML
    void submitForgotPassword(ActionEvent event) throws IOException, ClassNotFoundException {
        client.requestPasswordReset(this.forgotPasswordEmail.getText(), this.forgotPasswordNewPassword.getText());
    }
    void clearConnectInputs() {
        ipaddressInput.setText("");
        portInput.setText("");
    }
    public boolean showUserInfo(String username, String email, String dateOfReg) {
        if (username == null || email == null || dateOfReg == null) {
            return false;
        }
        String displayFormat = "Username: " + username + "\nEmail: " + email + "\nRegistration Date: " + dateOfReg;
        Platform.runLater(()->{
            this.notificationBox.setText(displayFormat);
        });
        this.blur(this.infoDisplay, false);
        return true;
    }
    public void hideNotificationBox() {
        this.showUserInfo("PLACEHOLDER", "PLACEHOLDER", "PLACEHOLDER:PLACEHOLDER");
        this.blur(this.infoDisplay, true);
    }
    void clearLoginInputs() {
        usernameInput.setText("");
        passwordInput.setText("");
    }
    void clearRegisterInputs() {
        registerUsername.setText("");
        registerPassword.setText("");
        registerConfirmPassword.setText("");
        registerEmail.setText("");
    }
    void toggleConnect() {
        toggleConnect(!connectInfoBox.isDisable(), !connectInfoBox.isDisable());
    }
    void toggleConnect(boolean toggle, boolean hideButton) {
        blur(connectInfoBox, toggle);
        disconnect.setVisible(hideButton);
    }
    void toggleRegister(boolean toggle) {
        loginInfoBox.setVisible(toggle);
        showRegisterPage = !toggle;
        registerInfoBox.setVisible(!toggle);
    }
    public void toggleLoginOutline(boolean b) {
        this.toggleOutline(b, this.usernameInput);
        this.toggleOutline(b, this.passwordInput);
    }
    private void blur(Node node, boolean enableBlur) {
        node.setDisable(enableBlur);
        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(enableBlur ? 10 : 0);
        node.setEffect(blur);
    }
    public TextField getForgotPasswordEmail() {
        return this.forgotPasswordEmail;
    }
    public void setForgotPasswordEmailText(String text, boolean isVisible, boolean success) {
        this.forgotPasswordText.setText(text);
        this.forgotPasswordText.setTextFill(!success ? Color.RED : Color.GREEN);
        this.forgotPasswordText.setVisible(isVisible);
    }
    public void setForgotPasswordText(String text) {
        this.forgotPasswordP.setText(text);
        this.forgotPasswordP.setTextFill(Color.RED);
        this.forgotPasswordP.setVisible(true);
    }
    public void setForgotPasswordEmailText(String text, boolean isVisible) {
        this.forgotPasswordText.setText(text);
        this.forgotPasswordText.setTextFill(Color.WHITE);
        this.forgotPasswordText.setVisible(isVisible);
    }
    void toggleLogin() {
        toggleLogin(!loginInfoBox.isDisable(), !loginInfoBox.isDisable());
    }
    void toggleLogin(boolean toggle, boolean hideButton) {
        blur(loginInfoBox, toggle);
        logoutButton.setVisible(hideButton);
    }
    void toggleOutline(boolean toggle, TextField field) {
        if (toggle) {
            field.setStyle("" +
                    "-fx-border-color: RED;" +
                    "-fx-border-radius: 15px;");
        } else {
            field.setStyle("" +
                    "-fx-border-color: TRANSPARENT;" +
                    "-fx-border-radius: 15px;");
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        top.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        top.setOnMouseDragged(event -> {
            stage.setX(event.getScreenX() - xOffset);
            stage.setY(event.getScreenY() - yOffset);
        });
        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(10);
        loginInfoBox.setEffect(blur);
        loginInfoBox.setVisible(true);
        loginInfoBox.setDisable(true);
        logoutButton.setVisible(false);
        disconnect.setVisible(false);
        registerInfoBox.setVisible(false);
        this.connectInfoBox.setVisible(true);
        this.couldNotConnect.setVisible(false);
        this.connnecting.setVisible(false);

        this.regInvalidEmail.setVisible(false);
        this.regInvalidUsername.setVisible(false);
        this.regInvalidPasswordFormat.setVisible(false);
        this.regMatchPassword.setVisible(false);

        // boolean[] input retrieved from server once user tries to register a new account.
        regPageErrors(new String[]{null, null, null, null}, false);
        String conn = "Connecting";
        this.connectingVisual = new Thread(()->{
            long speed = 500L;
            int dotNum = 1;
            while (true) {
                try {
                    Thread.sleep(speed);
                    dotNum = dotNum++ >= 3 ? 0 : dotNum;
                    int tempDotNum = dotNum;
                    Platform.runLater(()->{
                        this.connectingLabel.setText(conn + ".".repeat(tempDotNum));
                    });
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        this.hideNotificationBox();
        this.registerSuccess.setVisible(false);
        this.forgotPasswordBox.setVisible(false);
    }

    private void showForgotPassword() {
        this.loginInfoBox.setVisible(false);
        this.registerInfoBox.setVisible(false);
        this.forgotPasswordBox.setVisible(true);
    }
    private void hideForgotPassword() {
        this.loginInfoBox.setVisible(true);
        this.registerInfoBox.setVisible(false);
        this.forgotPasswordBox.setVisible(false);
        this.registerSuccess.setVisible(false);
        this.forgotPasswordP.setVisible(false);
        this.forgotPasswordP.setText("");
        this.forgotPasswordP.setTextFill(Color.TRANSPARENT);
        this.forgotPasswordNewPassword.setText("");
        this.forgotPasswordEmail.setText("");
    }
    public void regPageErrors(String[] enableList, boolean syncColors) {
        regInvalidEmail.setVisible(enableList[0] != null);
        regInvalidEmail.setText(enableList[0] != null ? enableList[0] : "");

        regInvalidUsername.setVisible(enableList[1] != null);
        regInvalidUsername.setText(enableList[1] != null ? enableList[1] : "");

        regInvalidPasswordFormat.setVisible(enableList[2] != null);
        regInvalidPasswordFormat.setText(enableList[2] != null ? enableList[2] : "");

        regMatchPassword.setVisible(enableList[3] != null);
        regMatchPassword.setText(enableList[3] != null ? enableList[3] : "");

        // if enableList[x] == true, highlight in green, false highlight in false

        if (syncColors) {
            registerEmail.setStyle(((enableList[0] == null) ? "-fx-border-color: transparent;" : "-fx-border-color: #d13030;") + "-fx-border-radius: 25px");
            registerUsername.setStyle(((enableList[1] == null) ? "-fx-border-color: transparent;" : "-fx-border-color: #d13030;") + "-fx-border-radius: 25px");
            registerPassword.setStyle(((enableList[2] == null) ? "-fx-border-color: transparent;" : "-fx-border-color: #d13030;") + "-fx-border-radius: 25px");
            registerConfirmPassword.setStyle(((enableList[3] == null) ? "-fx-border-color: transparent;" : "-fx-border-color: #d13030;") + "-fx-border-radius: 25px");
        }

    }
    void showPassword(boolean toggle) {
        if (toggle) {
            if (this.passwordInput.getText().length() < 1)
                return;
            this.recentPassword = passwordInput.getText();
            this.passwordInput.setPromptText(recentPassword);
            this.passwordInput.setText("");
            //this.passwordInput.setDisable(true);
            this.passwordInput.positionCaret(this.passwordInput.getText().length());
        } else {
            this.passwordInput.setText(recentPassword);
            this.passwordInput.setPromptText("Password");
            //this.passwordInput.setDisable(false);
            this.passwordInput.positionCaret(this.passwordInput.getText().length());
        }
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    public void registerAccount(ActionEvent actionEvent) {
        String email = registerEmail.getText();
        String username = registerUsername.getText();
        String password = registerPassword.getText();
        String confirmPassword = registerConfirmPassword.getText();
        if (!password.equals(confirmPassword)) {
            this.regPageErrors(new String[]{null, null, null, "Passwords do not match."}, true);
            return;
        }
        this.client.requestRegister(email, username, password, confirmPassword);
    }
    public void registerGoBack(ActionEvent actionEvent) {
        hideForgotPassword();
        this.setForgotPasswordEmailText("", false);
        this.toggleOutline(false, this.forgotPasswordEmail);
    }
    public void toggleRegisterSuccess(boolean b) {
        this.registerSuccess.setVisible(b);
        this.blur(this.registerInfoBox, b);

        this.registerEmail.setText("");
        this.registerUsername.setText("");
        this.registerPassword.setText("");
        this.registerConfirmPassword.setText("");

        this.regPageErrors(new String[]{null, null, null, null}, true);
    }
    public void toggleConnecting(boolean b1, boolean b) throws InterruptedException {
        String conn = "Connecting";
        Platform.runLater(()->{
            this.connectingLabel.setText(conn);
        });
        new Thread(()->{
            synchronized (this.connectingVisual) {
                try {
                    if (b1) {
                        try {
                            this.connectingVisual.start();
                        } catch (Exception ignore){}
                        try {
                            this.connectingVisual.notify();
                        } catch (Exception ignore){}

                    } else {
                        this.connectingVisual.wait();
                    }
                } catch (InterruptedException ignore) {}
            }
        }).start();
        this.connnecting.setVisible(b1);
        this.blur(connectInfoBox, b);
    }
    public void toggleCannotConnect(boolean b) {
        this.connnecting.setVisible(!b);
        this.couldNotConnect.setVisible(b);
    }












}