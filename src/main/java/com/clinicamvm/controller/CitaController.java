package com.clinicamvm.controller;

import business.entities.Cita;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import persistence.daos.impl.CitaJDBCDAO;
import persistence.utils.JDBCUtils;

import javafx.util.Callback; // Importa esta clase desde javafx.util.Callback

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

public class CitaController {
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

    @FXML
    private TableColumn<Cita, Void> colButton;

    private CitaJDBCDAO citaJDBCDAO;

    ObservableList<Cita> citasPendientes;


    public void getPendiente(String userName) {

        Connection connection = JDBCUtils.getConnection();

        // Crear una instancia de CitaJDBCDAO con la conexión JDBC
        citaJDBCDAO = new CitaJDBCDAO(connection, userName);

        citasPendientes = citaJDBCDAO.obtenerLista();

        tablaPendientes.setItems(citasPendientes);

        colCita.setCellValueFactory(new PropertyValueFactory<Cita, Integer>("idCita"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Cita, String>("nombre"));
        colCliente.setCellValueFactory(new PropertyValueFactory<Cita, String>("idCliente"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Cita, Date>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<Cita, Time>("hora"));
        colMotivo.setCellValueFactory(new PropertyValueFactory<Cita, String>("descripcion"));
        colButton.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Cita, Void> call(TableColumn<Cita, Void> param) {
                return new TableCell<>() {
                    private final Button button = new Button("Abrir");
                    //button.getStyleClass().add("button-style");
                    {
                        button.setOnAction(event -> {
                            Cita cita = getTableView().getItems().get(getIndex());
                            int citaId = cita.getIdCita();
                            cargarCitaDetalle(citaId);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(button);
                        }
                    }
                };
            }
        });
    }

    public void cargarCitaDetalle(int idCita) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ui/Cita.fxml"));
            Parent root = loader.load();

            // Obteniendo el controlador del FXML cargado
            CitaDetalleController controller = loader.getController();

            // Configurando el ID de la cita en el controlador del detalle de la cita
            controller.setIdCita(idCita);

            // Creando la escena y mostrando la ventana
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Información Cita");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
