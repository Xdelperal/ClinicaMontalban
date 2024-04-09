package com.clinicamvm.controller;

import business.entities.Cita;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import persistence.daos.impl.CitaJDBCDAO;
import persistence.utils.JDBCUtils;


public class MainPanelController implements Initializable {

    @FXML
    private Label userNameLabel;

    @FXML
    private Pane pendientes;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private Pane realizadas;

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

    @FXML
    private TableView<Cita> tablaPendientes;

    @FXML
    private TableColumn<Cita, Integer> colCita;

    @FXML
    private TableColumn<Cita, String> colCliente;

    @FXML
    private TableColumn<Cita, String> colNombre;

    @FXML
    private TableColumn<Cita, java.sql.Date> colFecha;

    @FXML
    private TableColumn<Cita, Time> colHora;

    @FXML
    private TableColumn<Cita, String> colMotivo;

    @FXML
    private TableColumn<Cita, Void> colButton;

    private CitaJDBCDAO citaJDBCDAO;

    ObservableList<Cita> citasPendientes;


    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;

    public MainPanelController(){}


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        getPendientes();
        // Inicialización del controlador
        pendientes.setVisible(true);
        realizadas.setVisible(false);

        closeButton.setOnAction(event -> cerrarVentana());
        pendingButton.setOnAction(event -> {
            try {
                mostrarPendientes();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
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
    private void mostrarPendientes() throws IOException {

        // Mostrar pendientes y ocultar realizadas
        pendientes.setVisible(true);
        realizadas.setVisible(false);

        // Establecer la clase seleccionada en pendingButton
        pendingButton.getStyleClass().add("selected");
        madeButton.getStyleClass().remove("selected");
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
    private void abrirPaginaWeb() {
        // Abrir la página web en el navegador por defecto
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI("https://www.clinicamontalban.com"));
        } catch (Exception e) {
            e.printStackTrace();
        }
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



    public void getPendientes() {

        Connection connection = JDBCUtils.getConnection();

        // Crear una instancia de CitaJDBCDAO con la conexión JDBC
        citaJDBCDAO = new CitaJDBCDAO(connection, userNameLabel.getText());

        citasPendientes = citaJDBCDAO.obtenerLista();

        tablaPendientes.setItems(citasPendientes);

        colCita.setCellValueFactory(new PropertyValueFactory<Cita, Integer>("idCita"));
        colCliente.setCellValueFactory(new PropertyValueFactory<Cita, String>("idCliente"));
        colNombre.setCellValueFactory(new PropertyValueFactory<Cita, String>("nombre"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Cita, java.sql.Date>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<Cita, Time>("hora"));
        colMotivo.setCellValueFactory(new PropertyValueFactory<Cita, String>("descripcion"));
        colButton.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Cita, Void> call(TableColumn<Cita, Void> param) {
                return new TableCell<>() {
                    private final Button button = new Button("Abrir");
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

}
