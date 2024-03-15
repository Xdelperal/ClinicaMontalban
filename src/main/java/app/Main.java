package app;

import com.clinicamvm.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Cargar la vista del inicio de sesión
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/com/ui/login.fxml"));
        Parent loginRoot = loginLoader.load();

        // Configurar la escena del inicio de sesión
        Scene loginScene = new Scene(loginRoot);

        // Configurar el escenario principal con la escena del inicio de sesión
        primaryStage.setScene(loginScene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
