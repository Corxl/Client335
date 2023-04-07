package me.corxl.client335;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ClientMain extends Application {
    private Client client;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientMain.class.getResource("login-window.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ((LoginWindow) fxmlLoader.getController()).setStage(stage);
        scene.setFill(Color.TRANSPARENT);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}