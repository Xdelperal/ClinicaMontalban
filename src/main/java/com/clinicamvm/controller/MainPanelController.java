package com.clinicamvm.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static java.awt.SystemColor.window;

public class MainPanelController implements Initializable {

    @FXML
    private Label userNameLabel;

    @FXML
    private TableView pendientes;

    @FXML
    private TableView realizadas;

    @FXML
    private Button pendingButton;

    @FXML
    private Button madeButton;

    @FXML
    private Button closeButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización del controlador
    }

    // Método para actualizar el Label con el nombre de usuario
    public void updateUserNameLabel(String userName) {
        userNameLabel.setText("Usuario: " + userName);
    }





    public void cerrarVentana() {
        // Obtener la instancia de la ventana actual
        Stage stage = (Stage) closeButton.getScene().getWindow();
        // Cerrar la ventana actual
        stage.close();

        try {
            // Cargar el archivo FXML de la ventana de inicio de sesión
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ui/login.fxml"));
            Parent root = loader.load();

            // Crear una nueva escena
            Scene scene = new Scene(root);

            // Crear un nuevo escenario (ventana)
            Stage loginStage = new Stage();
            loginStage.setScene(scene);
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        // Ocultar los labels al inicio
        pendientes.setVisible(false);
        realizadas.setVisible(false);
        closeButton.setOnAction(event -> cerrarVentana());
        // Establecer listeners de acción para los botones
        pendingButton.setOnAction(event -> mostrarPendientes());
        madeButton.setOnAction(event -> mostrarRealizadas());
    }

    @FXML
    private void mostrarPendientes() {
        // Mostrar pendientes y ocultar realizadas
        pendientes.setVisible(true);
        realizadas.setVisible(false);

        // Establecer la clase seleccionada en pendingButton
        pendingButton.getStyleClass().add("selected");
        madeButton.getStyleClass().remove("selected");
    }

    @FXML
    private void mostrarRealizadas() {
        // Mostrar realizadas y ocultar pendientes
        realizadas.setVisible(true);
        pendientes.setVisible(false);
        // Establecer la clase seleccionada en madeButton
        madeButton.getStyleClass().add("selected");
        pendingButton.getStyleClass().remove("selected");
    }



    @FXML
    private void salir() {
        // Mostrar realizadas y ocultar pendientes

        // Establecer la clase seleccionada en madeButton
        madeButton.getStyleClass().add("selected");
        pendingButton.getStyleClass().remove("selected");
    }
}
