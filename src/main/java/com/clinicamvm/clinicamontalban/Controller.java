package com.clinicamvm.clinicamontalban;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private TextField fieldUsuario;

    @FXML
    private PasswordField fieldPass;

    @FXML
    private Label msgLabel;

    @FXML
    private Button buttonEntrap;

    @FXML
    public void comprobacion(ActionEvent e) {
        if (fieldUsuario.getText().isBlank() || fieldPass.getText().isBlank()) {
            msgLabel.setText("No has introducido datos!");
        } else {
            // Aquí puedes agregar la lógica para verificar el inicio de sesión
        }
    }
}