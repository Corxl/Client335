package me.corxl.client335;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ResourceBundle;

public class LoginWindow implements Initializable {
    @FXML
    private VBox connectInfoBox;

    @FXML
    private Button disconnect;

    @FXML
    private TextField ipaddressInput;

    @FXML
    private VBox loginInfoBox;

    @FXML
    private Button logoutButton;

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
    private TextField registerUsername;

    @FXML
    private HBox top;

    @FXML
    private TextField usernameInput;

    private double xOffset = 0;
    private double yOffset = 0;
    private Stage stage;
    private boolean showingPassword = true;
    private String recentPassword = "";
    private boolean loginLine = false;
    private boolean showRegisterPage = false;

    @FXML
    void loginClick(MouseEvent event) {
        loginLine = !loginLine;
        toggleLoginOutline(loginLine, passwordInput);
        toggleLoginOutline(loginLine, usernameInput);
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
        System.out.println("???");
        toggleRegister(showRegisterPage);
        System.out.println("???2");
    }
    @FXML
    void connectClick(ActionEvent event) throws UnknownHostException {
        System.out.println(InetAddress.getLocalHost().getHostAddress());
        try {
            new Client(this, ipaddressInput.getText().trim(), Integer.parseInt(portInput.getText().trim())).start();
        } catch (NumberFormatException e) {
            // port is not an integer
        }

    }
    void clearConnectInputs() {
        ipaddressInput.setText("");
        portInput.setText("");
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
    @FXML
    void logoutClick(ActionEvent event) {
        toggleLogin(true, true);
    }
    @FXML
    void disconnect(ActionEvent event) {
        toggleConnect(false, false);
        toggleLogin(true, false);
        toggleRegister(true);
    }
    @FXML
    void passwordInputTyped(KeyEvent event) {
        toggleLoginOutline(false, passwordInput);
        if (!this.showingPassword) {
            this.recentPassword = this.passwordInput.getPromptText() + this.passwordInput.getText();
            this.passwordInput.setPromptText(recentPassword);
            this.passwordInput.setText("");
        }
    }
    @FXML
    void usernameInputTyped(KeyEvent event) {
        toggleLoginOutline(false, usernameInput);
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
        registerInfoBox.setVisible(showRegisterPage);
    }

    private void blur(Node node, boolean enableBlur) {
        node.setDisable(enableBlur);
        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(enableBlur ? 10 : 0);
        node.setEffect(blur);
    }
    void toggleLogin() {
        toggleLogin(!loginInfoBox.isDisable(), !loginInfoBox.isDisable());
    }
    void toggleLogin(boolean toggle, boolean hideButton) {
        blur(loginInfoBox, toggle);
        logoutButton.setVisible(hideButton);
    }

    void toggleLoginOutline(boolean toggle, TextField field) {
//        if (toggle) {
//            field.setStyle("" +
//                    "-fx-border-color: RED;" +
//                    "-fx-border-radius: 15px;");
//        } else {
//            field.setStyle("" +
//                    "-fx-border-color: TRANSPARENT;" +
//                    "-fx-border-radius: 15px;");
//        }
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
        // boolean[] input retrieved from server once user tries to register a new account.
        regPageErrors(new boolean[]{false, false, false, false}, false);

    }

    private void regPageErrors(boolean[] enableList, boolean syncColors) {
        regInvalidEmail.setVisible(enableList[0]);

        regInvalidUsername.setVisible(enableList[1]);

        regInvalidPasswordFormat.setVisible(enableList[2]);

        regMatchPassword.setVisible(enableList[3]);
        // if enableList[x] == true, highlight in green, false highlight in false

        if (syncColors) {
            registerEmail.setStyle((!enableList[0] ? "-fx-border-color: #2bbd4b;" : "-fx-border-color: #d13030;") + "-fx-border-radius: 25px");
            registerUsername.setStyle((!enableList[1] ? "-fx-border-color: #2bbd4b;" : "-fx-border-color: #d13030;") + "-fx-border-radius: 25px");
            registerPassword.setStyle((!enableList[2] ? "-fx-border-color: #2bbd4b;" : "-fx-border-color: #d13030;") + "-fx-border-radius: 25px");
            registerConfirmPassword.setStyle((!enableList[3] ? "-fx-border-color: #2bbd4b;" : "-fx-border-color: #d13030;") + "-fx-border-radius: 25px");
        }

    }

    @FXML
    void showPassword(MouseEvent event) {
        showPassword(this.showingPassword);
        showingPassword = !showingPassword;
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

    @FXML
    void passChanged(KeyEvent event) {
        System.out.println("?????");
        if (!this.showingPassword) {
            this.recentPassword = this.passwordInput.getPromptText() + this.passwordInput.getText();
            this.passwordInput.setPromptText(recentPassword);
            this.passwordInput.setText("");
        }

    }

    public void registerAccount(ActionEvent actionEvent) {

    }

    public void registerGoBack(ActionEvent actionEvent) {
        toggleRegister(true);
    }
}