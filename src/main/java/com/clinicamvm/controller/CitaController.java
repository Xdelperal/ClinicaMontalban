package com.clinicamvm.controller;

import business.entities.Cita;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import persistence.utils.JDBCUtils;
import java.sql.*;

public class CitaController {
    @FXML
    private TableView<Cita> tablaPendientes;

    private String userName;

    public void setUserName(String userName) {
        this.userName = userName;
    }


}
