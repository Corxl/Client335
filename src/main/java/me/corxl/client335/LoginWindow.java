package me.corxl.client335;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
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
    private HBox top;

    @FXML
    private TextField usernameInput;

    private double xOffset = 0;
    private double yOffset = 0;
    private Stage stage;
    private boolean showingPassword = true;
    private String recentPassword = "";
    private boolean loginLine = false;

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
    void registerClick(MouseEvent event) {

    }

    @FXML
    void connectClick(ActionEvent event) {
        toggleLogin();
        toggleConnect();
    }
    @FXML
    void logoutClick(ActionEvent event) {
        toggleLogin(true, true);
    }
    @FXML
    void disconnect(ActionEvent event) {
        toggleConnect(false, false);
        toggleLogin(true, false);
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
        connectInfoBox.setDisable(toggle);
        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(toggle ? 10 : 0);
        connectInfoBox.setEffect(blur);
        disconnect.setVisible(hideButton);
    }
    void toggleLogin() {
        toggleLogin(!loginInfoBox.isDisable(), !loginInfoBox.isDisable());
    }
    void toggleLogin(boolean toggle, boolean hideButton) {
        loginInfoBox.setDisable(toggle);
        GaussianBlur blur = new GaussianBlur();
        blur.setRadius(toggle ? 10 : 0);
        loginInfoBox.setEffect(blur);
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
        loginInfoBox.setDisable(true);
        logoutButton.setVisible(false);
        disconnect.setVisible(false);
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
}