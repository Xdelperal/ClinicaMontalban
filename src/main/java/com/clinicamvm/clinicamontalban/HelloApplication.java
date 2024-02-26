package com.clinicamvm.clinicamontalban;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        FXMLLoader fxmlLoader2 = new FXMLLoader(HelloApplication.class.getResource("mainPanel.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        Scene scene2 = new Scene(fxmlLoader2.load(), 320, 240);

        stage.setTitle("ClinicaMontalban App");
        stage.setScene(scene);

        stage.show();


        stage.setScene(scene2);

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}