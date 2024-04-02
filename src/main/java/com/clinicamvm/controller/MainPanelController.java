package com.clinicamvm.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.layout.Pane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import com.clinicamvm.controller.CitaController;


public class MainPanelController implements Initializable {

    private CitaController citaController;

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


    private int seconds = 0;
    private int minutes = 0;
    private int hours = 0;

    //Columnas de la tabla pendiente


    public MainPanelController(){}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        citaController = new CitaController();
        // Inicialización del controlador
        pendientes.setVisible(true);
        //realizadas.setVisible(false);

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
        //realizadas.setVisible(false);
        pendingButton.getStyleClass().add("selected");
        madeButton.getStyleClass().remove("selected");

        // Obtener el contenido de userNameLabel
        String userName = userNameLabel.getText();

        // Llamar a la función getPendiente() de citaController pasando el nombre de usuario
        citaController.getPendiente(userName);
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
}
