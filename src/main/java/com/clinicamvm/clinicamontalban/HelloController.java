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
    public void buttonEntrap(ActionEvent e) {

        if(fieldUsuario.getText().isBlank() == true || fieldPass.getText().isBlank() == true){

                msgLabel.setText("No has introducido datos!");

        }else{




        }


    }


}