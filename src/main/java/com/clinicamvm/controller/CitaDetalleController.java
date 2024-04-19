package com.clinicamvm.controller;

import business.entities.Cita;
import business.entities.Medicamento;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import persistence.daos.impl.CitaJDBCDAO;
import persistence.daos.impl.MedicamentoJDBCDAO;
import persistence.utils.*;
import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

public class CitaDetalleController implements Initializable {

    @FXML
    private TableView<Medicamento> listaMedicamentos;

    @FXML
    private Label lblIdCita;
    private MedicamentoJDBCDAO medicamentoJDBCDAO;

    @FXML
    private Button buttonSearchMedicamentos;

    @FXML
    private TextField textMedicamento;

    @FXML
    private TextArea motivoCitaText,ObservacionCitaText;
    @FXML
    private Button informeButton;

    private int idCita;

    private SQLQueries sqlQueries;
    private CitaJDBCDAO citaJDBCDAO;

    // Otros elementos de la ventana y métodos necesarios


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        medicamentoJDBCDAO = new MedicamentoJDBCDAO();
        listaMedicamentos.setVisible(true);
        Connection connection = JDBCUtils.getConnection();
        citaJDBCDAO = new CitaJDBCDAO(connection);
        informeButton.setOnAction(event -> crearInforme());
        buttonSearchMedicamentos.setOnAction(event -> busquedaMedicamentos());
        showMedicamentos();

    }

    @FXML
    private void getBusqueda(){
        listaMedicamentos.getItems().clear();
        ObservableList<Medicamento> buscarLista = medicamentoJDBCDAO.buscar(textMedicamento.getText());
        listaMedicamentos.setItems(buscarLista);

    }



    public void setIdCita(int idCita) {
        // Aquí puedes usar el ID de la cita para cargar y mostrar la información correspondiente en la ventana
        lblIdCita.setText("Cita numero " + idCita);
        this.idCita = idCita;
    }
    public void setMotivo(int idCita) {
        // Aquí puedes usar el ID de la cita para cargar y mostrar la información correspondiente en la ventana

        motivoCitaText.setText(citaJDBCDAO.getMotivo(idCita));
    }

    @FXML
    public void crearInforme(){
        citaJDBCDAO.crearInforme(this.idCita,ObservacionCitaText.getText());
    }



    @FXML
    private void showMedicamentos() {
        listaMedicamentos.getItems().clear();
        ObservableList<Medicamento> tablaMedicamentos = medicamentoJDBCDAO.getMedicamentos();
        listaMedicamentos.setItems(tablaMedicamentos);
    }

    JDBCUtils recursos = new JDBCUtils();

    @FXML
    private void busquedaMedicamentos() {
        listaMedicamentos.getItems().clear();
        ObservableList<Medicamento> tablaMedicamentos = medicamentoJDBCDAO.getMedicamentos();
        ObservableList<Medicamento> medicamentosIbu =  recursos.buscarTextoEnMedicamentos(textMedicamento.getText(),tablaMedicamentos);
        // Agregar los elementos obtenidos a la TableView
        listaMedicamentos.setItems(medicamentosIbu);

    }

}
