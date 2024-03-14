package com.clinicamvm.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PruebaMain {

    @FXML
    private Label pendientes;

    @FXML
    private Label realizadas;

    @FXML
    private Button pendingButton;

    @FXML
    private Button madeButton;

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
