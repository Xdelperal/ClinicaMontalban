package com.clinicamvm.controller;

import business.entities.Cita;
import business.entities.Medicamento;
import business.entities.Personal;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import persistence.daos.impl.CitaJDBCDAO;
import persistence.daos.impl.MedicamentoJDBCDAO;
import persistence.utils.JDBCUtils;


public class MainPanelController implements Initializable {

    @FXML
    private Label userNameLabel, currentTime, countTime;

    @FXML
    private TableView<Cita> pendientes, realizadas, datosPaciente;

    @FXML
    private TableView<Medicamento> tablaMedicamentos;

    @FXML
    private TableColumn<Cita, Void> colButton;

    @FXML
    private Button closeButton, webClinica;

    @FXML
    private ToggleButton madeButton, pendingButton, tiposButton, presearch, searchButton;

    @FXML
    private Pane PanelBuscador, panelMedicamentos;

    @FXML
    private TextField pacienteDNI;

    @FXML
    private ChoiceBox<String> tiposMedicamento;

    private CitaJDBCDAO citaJDBCDAO;

    private Personal medico;

    private String todos = "Todos", userDni;

    private MedicamentoJDBCDAO medicamentoJDBCDAO;

    private ObservableList<Medicamento> listaMedicamentos;
    private int seconds = 0, minutes = 0, hours = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup toggleGroup = new ToggleGroup();
        pendingButton.setToggleGroup(toggleGroup);
        madeButton.setToggleGroup(toggleGroup);
        tiposButton.setToggleGroup(toggleGroup);
        presearch.setToggleGroup(toggleGroup);
        searchButton.setToggleGroup(toggleGroup);
        //recetarButton.setToggleGroup(toggleGroup);

        Connection connection = JDBCUtils.getConnection();
        citaJDBCDAO = new CitaJDBCDAO(connection);
        medicamentoJDBCDAO = new MedicamentoJDBCDAO();
        medico = new Personal();

        // Inicialización de las tablas.
        datosPaciente.setVisible(true);
        tablaMedicamentos.setVisible(true);

        // Tratamiento de paneles.
        pendientes.setVisible(true);
        realizadas.setVisible(false);
        PanelBuscador.setVisible(false);
        panelMedicamentos.setVisible(false);

        // Accionadores de los botones.
        closeButton.setOnAction(event -> cerrarVentana());
        pendingButton.setOnAction(event -> mostrarPendientes());
        madeButton.setOnAction(event -> mostrarRealizadas());
        searchButton.setOnAction(event -> getBusqueda());
        tiposButton.setOnAction(event -> mostrarMedicamentos());
        tiposMedicamento.setOnAction(event -> showMedicamentos());

        presearch.setOnAction(event -> mostrarBuscar());

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

        iniciar();
        dropDownTipos();
    }

    public void iniciar() {
        pendientes.setVisible(true);
        realizadas.setVisible(false);
        PanelBuscador.setVisible(false);
        panelMedicamentos.setVisible(false);
        getPendiente(this.medico);

    }


    public void setMedico(Personal medico){

        this.medico = medico;

    }
    public void updateUserNameLabel(Personal medico) {
        userNameLabel.setText(medico.getNombre() + " " + medico.getApellidos());

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
        PanelBuscador.setVisible(false);
        panelMedicamentos.setVisible(false);
        getPendiente(this.medico);
    }

    @FXML
    private void mostrarRealizadas() {
        realizadas.setVisible(true);
        pendientes.setVisible(false);
        PanelBuscador.setVisible(false);
        panelMedicamentos.setVisible(false);

        getRealizadas();
    }


    @FXML
    private void mostrarBuscar() {
        pacienteDNI.setText("");
        realizadas.setVisible(false);
        pendientes.setVisible(false);
        panelMedicamentos.setVisible(false);
        PanelBuscador.setVisible(true);
    }

    private void dropDownTipos(){
        ObservableList<Medicamento> listadoTipos = medicamentoJDBCDAO.getTipoMedicamento();
        tiposMedicamento.getItems().add(todos);
        for (Medicamento medicamento : listadoTipos) {
            tiposMedicamento.getItems().add(medicamento.gettNombre());
        }

    }

    private void mostrarMedicamentos(){
        panelMedicamentos.setVisible(true);
        pendientes.setVisible(false);
        realizadas.setVisible(false);
        PanelBuscador.setVisible(false);
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
            controller.setMotivo(idCita);
            // Creando la escena y mostrando la ventana
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            // Establecer la escena en la ventana
            stage.setScene(scene);
            stage.setResizable(true); // Permitir redimensionar la ventana


            // Obtener el tamaño de la pantalla
            Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
            double screenWidth = primaryScreenBounds.getWidth();
            double screenHeight = primaryScreenBounds.getHeight();

            // Establecer el tamaño de la ventana para que ocupe toda la pantalla
            stage.setWidth(screenWidth);
            stage.setHeight(screenHeight);

            // Centrar la ventana en la pantalla
            stage.centerOnScreen();
            stage.setTitle("Información Cita");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void getRealizadas() {

        // Limpiar los elementos existentes en la TableView
        realizadas.getItems().clear();

        ObservableList<Cita> listaRealizadas = citaJDBCDAO.obtenerLista("Realizada", medico.getDni());

        // Agregar los elementos obtenidos a la TableView
        realizadas.setItems(listaRealizadas);
    }

    @FXML
    private void getBusqueda(){

        datosPaciente.getItems().clear();
        ObservableList<Cita> buscarLista = citaJDBCDAO.buscar(pacienteDNI.getText());
        datosPaciente.setItems(buscarLista);

    }


    public void getPendiente(Personal persona) {

        Personal medico = new Personal();


        pendientes.getItems().clear();
        ObservableList<Cita> listaPendiente = citaJDBCDAO.obtenerLista("Pendiente", medico.getDni());

        colButton.setCellFactory(null);
        pendientes.setItems(listaPendiente);

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



    JDBCUtils recursos = new JDBCUtils();

    private void showMedicamentos() {
        tablaMedicamentos.getItems().clear();
        String grupoMedicamento = tiposMedicamento.getValue();
        ObservableList<Medicamento> listaMedicamentos;
        if (grupoMedicamento != todos) {
            listaMedicamentos = medicamentoJDBCDAO.getMedicamentos(grupoMedicamento);
        }else{
            listaMedicamentos = medicamentoJDBCDAO.getMedicamentos();
        }
        // Agregar los elementos obtenidos a la TableView
        tablaMedicamentos.setItems(listaMedicamentos);


    }












}
