package com.clinicamvm.controller;

import business.entities.Cita;
import business.entities.Medicamento;
import business.entities.Personal;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableView;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

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

    //<editor-fold defaultstate="collapsed" desc="Columnas de Pendientes">
    @FXML
    private TableColumn<Cita, Integer> colDNI;

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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Columnas de Realizadas">
    @FXML
    private TableColumn<Cita, Integer> colCita1;

    @FXML
    private TableColumn<Cita, Integer> colDNI1;

    @FXML
    private TableColumn<Cita, String> colNombre1;

    @FXML
    private TableColumn<Cita, Date> colFecha1;

    @FXML
    private TableColumn<Cita, Time> colHora1;

    @FXML
    private TableColumn<Cita, String> colMotivo1;

    @FXML
    private TableColumn<Cita, Void> modificar1;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Columnas de Busqueda">
    @FXML
    private TableColumn<Cita, Integer> colCita11;

    @FXML
    private TableColumn<Cita, Integer> colDNI11;

    @FXML
    private TableColumn<Cita, String> colNombre11;

    @FXML
    private TableColumn<Cita, Date> colFecha11;

    @FXML
    private TableColumn<Cita, Time> colHora11;

    @FXML
    private TableColumn<Cita, String> colMotivo11;
    //</editor-fold>

    @FXML
    private TableView<Medicamento> tablaMedicamentos;

    @FXML
    private Button closeButton, searchButton;

    @FXML
    private ToggleButton madeButton, pendingButton, tiposButton, presearch;

    @FXML
    private Pane PanelBuscador, panelMedicamentos;

    @FXML
    private TextField pacienteDNI;

    @FXML
    private JFXComboBox<String> tiposMedicamento;

    private CitaJDBCDAO citaJDBCDAO;

    //Expresion regular para el buscador de DNI.
    private final Pattern pattern = Pattern.compile("\\d{0,8}[a-zA-Z]?");

    //Declaramos como variable el stage para poder hacer verificaciones.
    private Stage detalleCitaStage;

    private Personal medico;

    private String todos = "Todos", userDni;

    private MedicamentoJDBCDAO medicamentoJDBCDAO;

    private ObservableList<Medicamento> listaMedicamentos;
    private int seconds = 0, minutes = 0, hours = 0;

    private Timeline updateTimeline;

    private boolean cerrarVentanaPorApertura = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ToggleGroup toggleGroup = new ToggleGroup();
        pendingButton.setToggleGroup(toggleGroup);
        madeButton.setToggleGroup(toggleGroup);
        tiposButton.setToggleGroup(toggleGroup);
        presearch.setToggleGroup(toggleGroup);
        //searchButton.setToggleGroup(toggleGroup);

        toggleGroup.selectToggle(pendingButton);

        Connection connection = JDBCUtils.getConnection();
        citaJDBCDAO = new CitaJDBCDAO(connection);
        medicamentoJDBCDAO = new MedicamentoJDBCDAO();
        medico = new Personal();

        // Inicialización de las tablas.
        datosPaciente.setVisible(true);
        tablaMedicamentos.setVisible(true);

        // Tratamiento de paneles.
        pendientes.setVisible(false);
        realizadas.setVisible(false);
        PanelBuscador.setVisible(false);
        panelMedicamentos.setVisible(false);

        tiposMedicamento.setOnAction(event -> showMedicamentos());

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

        pacienteDNI.setTextFormatter(new TextFormatter<>(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                // Validar la nueva entrada con la expresión regular
                if (pattern.matcher(change.getControlNewText()).matches()) {
                    return change;
                } else {
                    // Si la entrada no coincide con la expresión regular, se rechaza
                    return null;
                }
            }
        }));

        setupAutoUpdate();
    }


    private void setupAutoUpdate() {
        updateTimeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(5), event -> {
            if (pendientes.isVisible()) {
                getPendiente();
            } else if (realizadas.isVisible()) {
                getRealizadas();
            }
        }));
        updateTimeline.setCycleCount(Animation.INDEFINITE);
        updateTimeline.play();
    }

    public void iniciar() {
        pendientes.setVisible(true);
        realizadas.setVisible(false);
        PanelBuscador.setVisible(false);
        panelMedicamentos.setVisible(false);
        getPendiente();
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
        getPendiente();
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
        // Establecer "todos" como el valor predeterminado
        tiposMedicamento.getSelectionModel().selectFirst();
    }

    @FXML
    private void mostrarMedicamentos(){
        panelMedicamentos.setVisible(true);
        pendientes.setVisible(false);
        realizadas.setVisible(false);
        PanelBuscador.setVisible(false);
    }

    @FXML
    private void abrirPaginaWeb() {
        try {
            // Comprobar el sistema operativo
            String os = System.getProperty("os.name").toLowerCase();
            Runtime rt = Runtime.getRuntime();
            if (os.contains("win")) {
                // Windows
                rt.exec("rundll32 url.dll,FileProtocolHandler https://www.clinicamontalban.com");
            } else if (os.contains("nix") || os.contains("nux") || os.contains("aix")) {
                // Linux
                rt.exec("xdg-open https://www.clinicamontalban.com");
            } else if (os.contains("mac")) {
                // Mac OS
                rt.exec("open https://www.clinicamontalban.com");
            } else {
                // Otros sistemas operativos, intenta con Desktop.browse
                java.awt.Desktop.getDesktop().browse(new java.net.URI("https://www.clinicamontalban.com"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarCitaDetalle(int idCita, boolean tipo) {
        if (detalleCitaStage != null) {
            // Si ya hay un detalle de cita abierto, preguntar si se desea cerrar
            Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
            confirmDialog.setTitle("Confirmar");
            confirmDialog.setHeaderText(null);

            // Crear el VBox
            VBox vbox = new VBox();

            // Crear el texto normal
            Label labelNormal = new Label("¿Estás seguro que quieres cerrar la pestaña actual de cita y abrir otra nueva?");
            vbox.getChildren().add(labelNormal);

            // Crear el texto en negrita y color rojo
            Label labelNegrita = new Label("Perderás las modificaciones actuales.");
            labelNegrita.setStyle("-fx-font-weight: bold;");
            labelNegrita.setTextFill(Color.RED);
            vbox.getChildren().add(labelNegrita);

            // Establecer el contenido del diálogo como el VBox
            confirmDialog.getDialogPane().setContent(vbox);

            // Mostrar el diálogo de confirmación y esperar la respuesta del usuario
            confirmDialog.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // El usuario decidió cerrar el detalle de cita actual
                    detalleCitaStage.close();
                    abrirNuevoDetalleCita(idCita, false);
                }
            });
        } else if (tipo){
            abrirNuevoDetalleCita(idCita, true);
        } else {
            abrirNuevoDetalleCita(idCita, false);
        }
    }

    // Modificar la función abrirNuevoDetalleCita() para establecer la bandera cuando se está cerrando la ventana
    private void abrirNuevoDetalleCita(int idCita, boolean tipo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ui/Cita.fxml"));
            Parent root = loader.load();

            // Obteniendo el controlador del FXML cargado
            CitaDetalleController controller = loader.getController();

            //Aquí declaramos que es una cita a modificar con el booleano, para que inicie otro metodo en el controlador al inicializarse.
            if(tipo) {
                controller.setReceta(idCita);
            } else {
                controller.setBoolean(false);
            }

            // Configurando el ID de la cita en el controlador del detalle de la cita
            controller.setCita(idCita, medico.getDni());
            controller.setMotivo(idCita);

            // Creando la escena y mostrando la ventana
            Scene scene = new Scene(root);
            Stage stage = new Stage();

            Image icono = new Image(getClass().getResourceAsStream("/com/ui/img/logo.png"), 200, 200, true, true);
            stage.getIcons().add(icono);

            // Establecer la escena en la ventana
            stage.setScene(scene);
            stage.setResizable(true); // Permitir redimensionar la ventana

            // Mantener la referencia al nuevo detalle de cita abierto
            detalleCitaStage = stage;

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

            // Establecer un controlador de evento para el cierre de la ventana
            stage.setOnCloseRequest(event -> {
                if (!cerrarVentanaPorApertura) {
                    event.consume(); // Consume el evento para evitar el cierre automático de la ventana

                    // Crear el VBox
                    VBox vbox = new VBox();

                    // Crear el texto normal
                    Label labelNormal = new Label("¿Estás seguro que quieres cerrar la pestaña actual de cita?");
                    vbox.getChildren().add(labelNormal);

                    // Crear el texto en negrita y color rojo
                    Label labelNegrita = new Label("Perderás las modificaciones actuales.");
                    labelNegrita.setStyle("-fx-font-weight: bold;");
                    labelNegrita.setTextFill(Color.valueOf("red")); // Convertir el Color a Paint
                    vbox.getChildren().add(labelNegrita);

                    // Establecer el contenido del diálogo como el VBox
                    Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
                    confirmDialog.setTitle("Confirmar");
                    confirmDialog.setHeaderText(null);
                    confirmDialog.getDialogPane().setContent(vbox);

                    // Mostrar el diálogo de confirmación y esperar la respuesta del usuario
                    confirmDialog.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            // El usuario decidió cerrar la ventana actual
                            detalleCitaStage.close();

                            // Establecer detalleCitaStage en null después de cerrar la ventana
                            detalleCitaStage = null;
                        }
                    });
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getPendiente() {
        pendientes.getItems().clear();
        ObservableList<Cita> listaPendiente = citaJDBCDAO.obtenerLista("Pendiente", medico.getDni());
        pendientes.setItems(listaPendiente);

        // Configurar las fábricas de valores de celdas para cada columna
        colDNI.setCellValueFactory(new PropertyValueFactory<>("DNI"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        // Configurar el botón en la columna de acceso
        colButton.setCellFactory(new Callback<TableColumn<Cita, Void>, TableCell<Cita, Void>>() {
            @Override
            public TableCell<Cita, Void> call(TableColumn<Cita, Void> param) {
                return new TableCell<>() {
                    private final Button button = new Button("Abrir");
                    {
                        button.setOnAction(event -> {
                            Cita cita = getTableView().getItems().get(getIndex());
                            int citaId = cita.getIdCita();
                            cargarCitaDetalle(citaId, false);
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

    @FXML
    private void getRealizadas() {
        // Limpiar los elementos existentes en la TableView
        realizadas.getItems().clear();
        ObservableList<Cita> listaRealizadas = citaJDBCDAO.obtenerLista("Realizada", medico.getDni());
        realizadas.setItems(listaRealizadas);

        // Configurar las fábricas de valores de celdas para cada columna
        colCita1.setCellValueFactory(new PropertyValueFactory<>("idCita"));
        colDNI1.setCellValueFactory(new PropertyValueFactory<>("DNI"));
        colNombre1.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFecha1.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora1.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colMotivo1.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

        modificar1.setCellFactory(new Callback<TableColumn<Cita, Void>, TableCell<Cita, Void>>() {
            @Override
            public TableCell<Cita, Void> call(TableColumn<Cita, Void> param) {
                return new TableCell<>() {
                    private final Button button = new Button("Modificar");
                    {
                        button.setOnAction(event -> {
                            Cita cita = getTableView().getItems().get(getIndex());
                            int citaId = cita.getIdCita();
                            cargarCitaDetalle(citaId, true);
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

    @FXML
    private void getBusqueda() {
        datosPaciente.getItems().clear();

        // Validar el DNI antes de ejecutar la búsqueda
        String dni = pacienteDNI.getText();
        if (!pattern.matcher(dni).matches()) {
            // Mostrar un mensaje de error o tomar otra acción si el DNI no es válido
            return;
        }

        // Ejecutar la búsqueda con el DNI válido
        ObservableList<Cita> buscarLista = citaJDBCDAO.buscar(dni);
        datosPaciente.setItems(buscarLista);

        // Configurar manualmente las celdas de las columnas
        colCita11.setCellValueFactory(new PropertyValueFactory<>("idCita"));
        colDNI11.setCellValueFactory(new PropertyValueFactory<>("DNI"));
        colNombre11.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colFecha11.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora11.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colMotivo11.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
    }

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
