package com.clinicamvm.controller;

import business.entities.Cita;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import persistence.daos.impl.CitaJDBCDAO;
import persistence.utils.JDBCUtils;
import java.sql.*;

public class CitaController {
    @FXML
    private TableView<Cita> tablaPendientes;

    @FXML
    private TableColumn<?,?> colCita;

    @FXML
    private TableColumn<?,?> colCliente;

    @FXML
    private TableColumn<?,?> colNombre;
    @FXML
    private TableColumn<?,?> colFecha;

    @FXML
    private TableColumn<?,?> colHora;

    @FXML
    private TableColumn<?,?> colMotivo;


    private CitaJDBCDAO listaCitas;

    ObservableList<Cita> citas;

    public void getPendiente() {

        citas = listaCitas.obtenerLista();

        for(Cita todaCita : citas) {

            colCita.setCellValueFactory(new PropertyValueFactory<>(String.valueOf(todaCita.getIdCita())));
            colNombre.setCellValueFactory(new PropertyValueFactory<>(todaCita.getNombre()));
            colCliente.setCellValueFactory(new PropertyValueFactory<>(String.valueOf(todaCita.getIdCliente())));
            colFecha.setCellValueFactory(new PropertyValueFactory<>(String.valueOf(todaCita.getFecha())));
            colHora.setCellValueFactory(new PropertyValueFactory<>(String.valueOf(todaCita.getHora())));
            colMotivo.setCellValueFactory(new PropertyValueFactory<>(todaCita.getDescripcion()));
        }


    }
}
