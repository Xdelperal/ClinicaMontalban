package com.clinicamvm.controller;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import persistence.utils.JDBCUtils;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;

import business.entities.Cita;
import javafx.util.Duration;
import javafx.scene.control.TableColumn;


import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.sql.*;


public class MainPanelController implements Initializable {

    @FXML
    private TableColumn colCita,colDNI,colNombre,colFecha,colHora,colMotivo;

    @FXML
    private Label userNameLabel;

    @FXML
    private TableView pendientes;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TableView realizadas;

    @FXML
    private Button pendingButton;

    @FXML
    private Button madeButton;

    @FXML
    private Button closeButton;

    @FXML
    private Label currentTime;

    @FXML
    private Label countTime;

    @FXML
    private Button webClinica;

    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Inicialización del controlador
        pendientes.setVisible(true);
        realizadas.setVisible(false);

        closeButton.setOnAction(event -> cerrarVentana());
        pendingButton.setOnAction(event -> mostrarPendientes());
        madeButton.setOnAction(event -> mostrarRealizadas());

        // Iniciar el timeline para actualizar el tiempo actual y el tiempo transcurrido
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            // Obtener la hora actual y formatearla como deseado
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy  -  HH:mm:ss");
            String formattedDate = dateFormat.format(new Date());

            // Actualizar el texto del Label con el tiempo actual
            currentTime.setText(formattedDate);

            // Incrementar los segundos del contador
            seconds++;
            // Ajustar los minutos y los segundos si es necesario
            if (seconds >= 60) {
                seconds = 0;
                minutes++;
                if (minutes >= 60) {
                    minutes = 0;
                    hours++;
                }
            }
            // Actualizar el texto del Label con el tiempo transcurrido
            String formattedTime = String.format("%d:%02d:%02d", hours, minutes, seconds);
            countTime.setText(formattedTime);
        }));
        timeline.setCycleCount(Animation.INDEFINITE); // Ejecutar continuamente
        timeline.play(); // Iniciar la animación
    }

    // Método para actualizar el Label con el nombre de usuario
    public void updateUserNameLabel(String userName) {
        userNameLabel.setText(userName);
    }


    public void cerrarVentana() {
        // Obtener la instancia de la ventana actual
        Stage stage = (Stage) closeButton.getScene().getWindow();
        // Cerrar la ventana actual
        stage.close();

        try {
            // Cargar el archivo FXML de la ventana de inicio de sesión
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ui/login.fxml"));
            Parent root = loader.load();

            // Crear una nueva escena
            Scene scene = new Scene(root);

            // Crear un nuevo escenario (ventana)
            Stage loginStage = new Stage();
            loginStage.setScene(scene);
            loginStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    private void mostrarPendientes() {
        // Mostrar pendientes y ocultar realizadas
        pendientes.setVisible(true);
        realizadas.setVisible(false);

        // Establecer la clase seleccionada en pendingButton
        pendingButton.getStyleClass().add("selected");
        madeButton.getStyleClass().remove("selected");
        getPendiente();
    }

    @FXML
    private void mostrarRealizadas() {
        // Mostrar realizadas y ocultar pendientes
        realizadas.setVisible(true);
        pendientes.setVisible(false);
        // Establecer la clase seleccionada en madeButton
        madeButton.getStyleClass().add("selected");
        pendingButton.getStyleClass().remove("selected");
    }



    @FXML
    private void salir() {
        // Mostrar realizadas y ocultar pendientes

        // Establecer la clase seleccionada en madeButton
        madeButton.getStyleClass().add("selected");
        pendingButton.getStyleClass().remove("selected");
    }



    @FXML
    private void getPendiente() {
        try {
            // Establecer la conexión a la base de datos
            Connection connection = JDBCUtils.getConnection();

            // Consulta SQL para obtener el ID del trabajador
            String sql = "SELECT idTrabajador FROM personal WHERE DNI = ?";

            try {
                // Preparar la declaración SQL
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, userNameLabel.getText());

                // Ejecutar la consulta
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {

                    // Consulta SQL para obtener las citas pendientes con el nombre del cliente
                    String sqlCitas = "SELECT cita.idCita, cita.idCliente, persona.nombre, cita.estado, cita.fecha, cita.hora, cita.descripcion " +
                            "FROM cita " +
                            "INNER JOIN cliente ON cita.idCliente = cliente.idCliente " +
                            "INNER JOIN persona ON cliente.DNI = persona.DNI " +
                            "INNER JOIN personal ON personal.idTrabajador = cita.idTrabajador " +
                            "WHERE personal.DNI = ?";


                    // Preparar la declaración SQL para la segunda consulta
                    PreparedStatement statementCitas = connection.prepareStatement(sqlCitas);
                    statementCitas.setString(1, userNameLabel.getText());

                    // Ejecutar la segunda consulta
                    ResultSet resultSetCitas = statementCitas.executeQuery();

                    // Procesar el resultado

                        // Hacer lo que necesites con los datos de la cita
                        pendientes.getItems().clear();

                        // Recorrer el resultado de la consulta
                        while (resultSetCitas.next()) {
                            // Obtener los valores de las columnas
                            int idCita = resultSetCitas.getInt("idCita");
                            int idCliente = resultSetCitas.getInt("idCliente");
                            String nombre = resultSetCitas.getString("nombre");
                            String estado = resultSetCitas.getString("estado");
                            Date fecha = resultSetCitas.getDate("fecha");
                            Time hora = resultSetCitas.getTime("hora");
                            String descripcion = resultSetCitas.getString("descripcion");

                            // Crear un objeto Cita con los datos obtenidos
                            Cita nuevaCita = new Cita(idCita, idCliente, nombre, estado, (java.sql.Date) fecha, hora, descripcion);

                            // Añadir la cita a la tabla
                            pendientes.getItems().add(nuevaCita);


                            colCita.setCellValueFactory(new PropertyValueFactory<>("idCita"));
                            colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
                            colDNI.setCellValueFactory(new PropertyValueFactory<>("idCliente"));
                            colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
                            colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
                            colMotivo.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
                        }
                    }



                // Cerrar recursos
                resultSet.close();
                statement.close();
                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
    } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }






    @FXML
    private void abrirPaginaWeb() {
        // Abrir la página web en el navegador por defecto
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI("https://www.clinicamontalban.com"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
