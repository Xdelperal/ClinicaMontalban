package com.clinicamvm.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainPanelController {

    @FXML
    private Label userNameLabel;

    public void initialize() {
        // Aquí puedes realizar cualquier inicialización necesaria
    }

    // Método para actualizar el Label con el nombre de usuario
    public void updateUserNameLabel(String userName) {
        userNameLabel.setText("Usuario: " + userName);
    }

    // Método para cargar el MainPanel y mostrarlo en el escenario
    public void cargarMainPanel() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ui/main.fxml"));
            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);

            // Obtener el controlador del MainPanel
            MainPanelController controller = fxmlLoader.getController();

            // Llamar al método para actualizar el Label
            controller.updateUserNameLabel("NombreDeUsuario"); // Puedes pasar el nombre de usuario como argumento

            // Obtener el Stage actual y establecer la escena
            Stage stage = (Stage) userNameLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
