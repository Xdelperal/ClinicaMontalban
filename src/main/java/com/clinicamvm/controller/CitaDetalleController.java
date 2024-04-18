package com.clinicamvm.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import persistence.daos.impl.CitaJDBCDAO;
import persistence.utils.JDBCUtils;

import javax.swing.plaf.basic.BasicButtonUI;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class CitaDetalleController implements Initializable {

    @FXML
    private Label lblIdCita;
    @FXML
    private TextArea motivoCitaText,ObservacionCitaText;
    @FXML
    private Button informeButton;

    private int idCita;




    private CitaJDBCDAO citaJDBCDAO;

    // Otros elementos de la ventana y métodos necesarios



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Connection connection = JDBCUtils.getConnection();
        citaJDBCDAO = new CitaJDBCDAO(connection);
        informeButton.setOnAction(event -> crearInforme());


    }




    public void setIdCita(int idCita) {
        // Aquí puedes usar el ID de la cita para cargar y mostrar la información correspondiente en la ventana
        lblIdCita.setText("Cita numero " + idCita);
        this.idCita = idCita;
    }
    public void setMotivo(String descripcion) {
        // Aquí puedes usar el ID de la cita para cargar y mostrar la información correspondiente en la ventana
        motivoCitaText.setText(descripcion);
    }



    public void crearInforme(){


        citaJDBCDAO.crearInforme(this.idCita,ObservacionCitaText.getText());





    }



}
