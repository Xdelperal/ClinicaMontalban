package com.clinicamvm.controller;

import business.entities.Cita;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import persistence.daos.impl.CitaJDBCDAO;
import persistence.utils.JDBCUtils;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CitaController implements Initializable {
    @FXML
    private TableView<Cita> tablaPendientes;

    @FXML
    private TableColumn<Cita, Integer> colCita;

    @FXML
    private TableColumn<Cita, String> colCliente;

    @FXML
    private TableColumn<Cita, String> colNombre;
    @FXML
    private TableColumn<Cita, Date> colFecha;

    @FXML
    private TableColumn<Cita, Time> colHora;

    @FXML
    private TableColumn<Cita, String> colMotivo;

    private CitaJDBCDAO citaJDBCDAO;

    ObservableList<Cita> citas;


    public void getPendiente() {

        Connection connection = JDBCUtils.getConnection();

        // Crear una instancia de CitaJDBCDAO con la conexión JDBC
        citaJDBCDAO = new CitaJDBCDAO(connection);

        citas = citaJDBCDAO.obtenerLista();

        tablaPendientes.setItems(citas);

        // No necesitas un bucle para configurar las celdas, PropertyValueFactory lo hará automáticamente.
        colCita.setCellValueFactory(new PropertyValueFactory<Cita, Integer>("idCita"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Cita, String>("nombre"));
        colCliente.setCellValueFactory(new PropertyValueFactory<Cita, String>("idCliente"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Cita, Date>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<Cita, Time>("hora"));
        colMotivo.setCellValueFactory(new PropertyValueFactory<Cita, String>("descripcion"));


    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getPendiente();
    }
}
