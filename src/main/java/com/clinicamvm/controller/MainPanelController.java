package com.clinicamvm.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainPanelController {
    @FXML
    private Label TEST; // El Label donde se mostrará el nombre de usuario

    private String loggedInUsername; // Nombre de usuario actualmente iniciado sesión

    // Método para establecer el nombre de usuario después de iniciar sesión
    public void setUserAfterLogin(String username) {
        this.loggedInUsername = username;
        updateLoggedInUserLabel(); // Actualizar el texto del Label
    }

    // Método para actualizar el texto del Label con el nombre de usuario actual
    private void updateLoggedInUserLabel() {
        if (TEST != null && loggedInUsername != null) {
            TEST.setText("Usuario: " + loggedInUsername);
        }
    }

    // Otros métodos y lógica de tu controlador
}
