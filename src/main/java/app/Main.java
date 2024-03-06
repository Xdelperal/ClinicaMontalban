package app;

import com.clinicamvm.controller.LoginController;
import com.clinicamvm.controller.MainPanelController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    // En el método start de tu clase Main

    @Override
    public void start(Stage loginStage) throws IOException {
        // Crear un nuevo Stage para la vista del panel principal
        Stage mainPanelStage = new Stage();

        // Cargar la vista del panel principal
        FXMLLoader mainPanelLoader = new FXMLLoader(getClass().getResource("/com/ui/mainPanel.fxml"));
        Parent mainPanelRoot = mainPanelLoader.load();
        MainPanelController mainPanelController = mainPanelLoader.getController();

        // Cargar la vista del inicio de sesión
        FXMLLoader loginLoader = new FXMLLoader(getClass().getResource("/com/ui/login.fxml"));
        Parent loginRoot = loginLoader.load();
        LoginController loginController = loginLoader.getController();

        // Configurar la referencia al controlador del panel principal en el controlador de inicio de sesión
        loginController.setMainPanelController(mainPanelController);

        // Configurar la escena y mostrarla en el escenario
        Scene loginScene = new Scene(loginRoot);
        loginStage.setScene(loginScene);
        loginStage.show();

        // Configurar la escena del panel principal y mostrarla en el escenario
        Scene mainPanelScene = new Scene(mainPanelRoot);
        mainPanelStage.setScene(mainPanelScene);
    }


    public static void main(String[] args) {
        launch();
    }
}
