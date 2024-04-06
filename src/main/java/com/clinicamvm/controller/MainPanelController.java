package com.clinicamvm.controller;

import business.entities.Cita;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import persistence.daos.impl.CitaJDBCDAO;
import persistence.exceptions.DAOException;
import persistence.utils.JDBCUtils;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class MainPanelController implements Initializable {


    @FXML
    private Label userNameLabel, currentTime, countTime;
    @FXML
    private TableView<Cita> pendientes, realizadas;
    @FXML
    private Button pendingButton, madeButton, closeButton, webClinica, testBuscar;
    @FXML
    private TextField pacienteDNI;
    private CitaJDBCDAO citaJDBCDAO;
    private int seconds = 0, minutes = 0, hours = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        Connection connection = JDBCUtils.getConnection();
        citaJDBCDAO = new CitaJDBCDAO(connection);

        pendientes.setVisible(true);
        realizadas.setVisible(false);

        closeButton.setOnAction(event -> cerrarVentana());
        pendingButton.setOnAction(event -> mostrarPendientes());
        madeButton.setOnAction(event -> mostrarRealizadas());
    //  testBuscar.setOnAction(event -> buscar());

        Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(1), event -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  -  HH:mm:ss");
            String formattedDate = dateFormat.format(new Date());
            currentTime.setText(formattedDate);
            seconds++;
            if (seconds >= 60) {
                seconds = 0;
                minutes++;
                if (minutes >= 60) {
                    minutes = 0;
                    hours++;
                }
            }
            String formattedTime = String.format("%d:%02d:%02d", hours, minutes, seconds);
            countTime.setText(formattedTime);
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void updateUserNameLabel(String userName) {
        userNameLabel.setText(userName);
    }

    public void cerrarVentana() {
        Stage stage = (Stage) closeButton.getScene().getWindow();
        stage.close();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ui/login.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage loginStage = new Stage();
            loginStage.setScene(scene);
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void mostrarPendientes() {
        pendientes.setVisible(true);
        realizadas.setVisible(false);
        pendingButton.getStyleClass().add("selected");
        madeButton.getStyleClass().remove("selected");
        getPendiente();
       // buscar();
    }

    @FXML
    private void mostrarRealizadas() {
        realizadas.setVisible(true);
        pendientes.setVisible(false);
        madeButton.getStyleClass().add("selected");
        pendingButton.getStyleClass().remove("selected");
       getRealizadas();
    }

    @FXML
    private void buscar() {
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement statementCitas = connection.prepareStatement("SELECT cita.idCita, cita.idCliente, persona.nombre, cita.estado, cita.fecha, cita.hora, cita.descripcion " +
                     "FROM cita " +
                     "INNER JOIN cliente ON cita.idCliente = cliente.idCliente " +
                     "INNER JOIN persona ON cliente.DNI = persona.DNI " +
                     "WHERE cliente.DNI = ?")) {

            statementCitas.setString(1, pacienteDNI.getText());
            ResultSet resultSetCitas = statementCitas.executeQuery();

            while (resultSetCitas.next()) {
                int idCita = resultSetCitas.getInt("idCita");
                int idCliente = resultSetCitas.getInt("idCliente");
                String nombre = resultSetCitas.getString("nombre");
                String estado = resultSetCitas.getString("estado");
                Date fecha = resultSetCitas.getDate("fecha");
                java.sql.Time hora = resultSetCitas.getTime("hora");
                String descripcion = resultSetCitas.getString("descripcion");
                Cita nuevaCita = new Cita(idCita, idCliente, nombre, estado, (java.sql.Date) fecha, hora, descripcion);
               // System.out.println(nuevaCita.getDatos());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void getPendiente() {
        ObservableList<Cita> listaPendiente = citaJDBCDAO.obtenerLista("Pendiente", userNameLabel.getText());

        // Limpiar los elementos existentes en la TableView
        pendientes.getItems().clear();

        // Agregar los elementos obtenidos a la TableView
        pendientes.setItems(listaPendiente);
}


    private void getRealizadas() {

        ObservableList<Cita> listaRealizadas = citaJDBCDAO.obtenerLista("Realizada", userNameLabel.getText());


        // Limpiar los elementos existentes en la TableView
        realizadas.getItems().clear();

        // Agregar los elementos obtenidos a la TableView
        realizadas.setItems(listaRealizadas);
    }



    @FXML
    private void abrirPaginaWeb(){





    }
}
