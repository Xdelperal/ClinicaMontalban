package com.clinicamvm.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPanelController implements Initializable {

    @FXML
    private Label userNameLabel;

    @FXML
    private Label pendientes;

    @FXML
    private Label realizadas;

    @FXML
    private Button pendingButton;

    @FXML
    private Button madeButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización del controlador
    }

    // Método para actualizar el Label con el nombre de usuario
    public void updateUserNameLabel(String userName) {
        userNameLabel.setText("Usuario: " + userName);
    }

    @FXML
    private void initialize() {
        // Ocultar los labels al inicio
        pendientes.setVisible(true);
        realizadas.setVisible(false);

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

}
