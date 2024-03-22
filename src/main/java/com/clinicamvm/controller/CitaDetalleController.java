package com.clinicamvm.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CitaDetalleController {

    @FXML
    private Label lblIdCita;

    // Otros elementos de la ventana y métodos necesarios

    public void setIdCita(int idCita) {
        // Aquí puedes usar el ID de la cita para cargar y mostrar la información correspondiente en la ventana
        lblIdCita.setText("ID de la cita: " + idCita);
    }

    // Otros métodos de inicialización y lógica de la ventana
}
