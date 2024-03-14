package com.clinicamvm.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class MainPanelController implements Initializable {

    @FXML
    private Label userNameLabel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización del controlador
    }

    // Método para actualizar el Label con el nombre de usuario
    public void updateUserNameLabel(String userName) {
        userNameLabel.setText("Usuario: " + userName);
    }


}
