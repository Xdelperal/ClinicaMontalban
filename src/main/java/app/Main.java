package app;

import com.clinicamvm.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;

public class Main extends Application {

    /**
     * El main principal que se encarga de abrir la escena login.
     * @param primaryStage
     * @throws IOException
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Cargar la vista del inicio de sesión
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/com/ui/login.fxml"));
        Parent loginRoot = loginLoader.load();

        // Configurar la escena del inicio de sesión
        Scene loginScene = new Scene(loginRoot);

        // Configurar el escenario principal con la escena del inicio de sesión
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Login");

        // Cargar el ícono de la aplicación
        Image icono = new Image(getClass().getResourceAsStream("/com/ui/img/logo.png"));
        primaryStage.getIcons().add(icono);

        primaryStage.show();

        // Obtener el controlador y establecer la etapa
        LoginController loginController = loginLoader.getController();
        loginController.setStage(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
