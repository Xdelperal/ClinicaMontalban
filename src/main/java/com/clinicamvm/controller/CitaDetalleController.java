package com.clinicamvm.controller;

import business.entities.Cita;
import business.entities.Medicamento;
import business.entities.Personal;
import business.entities.Receta;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import persistence.daos.impl.CitaJDBCDAO;
import persistence.daos.impl.MedicamentoJDBCDAO;
import persistence.daos.impl.RecetaJDBCDAO;
import persistence.utils.JDBCUtils;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class CitaDetalleController implements Initializable {

    //<editor-fold defaultstate="collapsed" desc="Elementos FXML Receta">
    @FXML
    private TableView<Receta> tablaReceta;
    @FXML
    private TableColumn<Receta, String> dosisEstandar, nombreLista, cantidadDosis, comentario;
    @FXML
    private TableColumn<Receta, Date> fechaInicial, fechaFinal;
    @FXML
    private TableColumn<Receta, Button> eliminar;
    @FXML
    private JFXCheckBox soloInforme;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Elementos FXML Medicamento">
    @FXML
    private TableColumn<Medicamento, String> nombreMedicamento, dosisMedicamento;
    @FXML
    private TableColumn<Medicamento, Void> añadirMedicamento;
    @FXML
    private TableView<Medicamento> listaMedicamentos;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Elementos FXML">
    @FXML
    private Label errorText, errorTextFecha, errorTextChoice, nombrePaciente, dniPaciente, fechaPaciente, eliminarMensaje;
    @FXML
    private Button generar, cerrarCita, eliminarCita;
    @FXML
    private TextField textMedicamento;
    @FXML
    private TextArea motivoCitaText, ObservacionCitaText, ObservacionMedicoText;
    @FXML
    private JFXComboBox<String> duracion;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Elementos normales">
    private int idCita, numeroReceta;
    private Personal personal;
    private boolean informeCreado = false;
    private CitaJDBCDAO citaJDBCDAO;
    private MedicamentoJDBCDAO medicamentoJDBCDAO;
    private ObservableList<Receta> listaReceta = FXCollections.observableArrayList();
    private ObservableList<Receta> listaRecetaExistente = FXCollections.observableArrayList();
    private ObservableList<Medicamento> tablaMedicamentos;
    private Map<Integer, Medicamento> mapaMedicamentos = new HashMap<>();
    private boolean estado;
    private Cita cita;
    //</editor-fold>

    /**
     * Este metodo es el que se inicia nada más inicializar el controlador. Lo más relevante es que
     * itera el hasMap para obtener todos los medicamentos por su ID nada más iniciar ya que hay otros metodos
     * que dependen de esta lista generada con el Hasmap.
     *
     * También hay campos que se inicializan en Visible(False), ya que dependiendo si estas modificando o creando se veran
     * diferentes labels y botones.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        medicamentoJDBCDAO = new MedicamentoJDBCDAO();
        Connection connection = JDBCUtils.getConnection();
        citaJDBCDAO = new CitaJDBCDAO(connection);
        generar.setOnAction(event -> crearInforme());
        tablaMedicamentos = medicamentoJDBCDAO.getMedicamentos();
        showMedicamentos(tablaMedicamentos);
        errorTextChoice.setVisible(false);

        for (Medicamento medicamento : tablaMedicamentos) {
            mapaMedicamentos.put(medicamento.getId(), medicamento);
        }

        eliminarMensaje.setVisible(false);
        eliminarCita.setVisible(false);

    }

    //<editor-fold defaultstate="collapsed" desc="Metodos de cita">

    /**
     * En estos tres metodos que vienen seteamos con la información del anterior controllador a este, la cita, estado, dni
     * información del paciente y el motivo.
     * @param idCita
     * @param dni
     */
    public void setCita(int idCita, String dni) {
        cita = citaJDBCDAO.getCita(dni, idCita);
        this.idCita = idCita;
        nombrePaciente.setText(cita.getNombre());
        dniPaciente.setText(cita.getDNI());
        fechaPaciente.setText(cita.getFecha() + " " + cita.getHora());
    }

    public void setBoolean(boolean setEstado) {
        this.estado = setEstado;
    }

    public void setMotivo(int idCita) {
        motivoCitaText.setText(citaJDBCDAO.getMotivo(idCita));
    }

    // Este metodo se encarga de cerrar la ventana y que la ventana sea nulla para que no haya errores.
    public void cerrarCita() {
        Stage stage = (Stage) cerrarCita.getScene().getWindow();
        stage.close();
        MainPanelController.detalleCitaStage = null;
    }

    /**
     * Basicamente crearemos el informe segun el booleano que declaramos en el controlador anterior,
     * ya que la misma ventana y controlador funciona tanto en crear o modificar las citas.
     */
    @FXML
    public void crearInforme() {
        if (estado) {
            actualizarInforme();
        } else {
            informeNuevo();
        }
    }

    /**
     * Aquí he intentado hacer lo mas modular posible lo de las alertas, pasandole por parametro
     * los textos ya que se repetia varias veces sobre la clase.
     */
    private void mostrarAlerta(String titulo, String mensaje, String detalle) {
        VBox vbox = new VBox();
        Label label = new Label(mensaje);
        label.setTextFill(Color.GREEN);
        label.setStyle("-fx-font-weight: bold; -fx-font-size: 13pt;");
        vbox.getChildren().add(label);

        if (detalle != null) {
            Label labelDetalle = new Label(detalle);
            labelDetalle.setTextFill(Color.GREEN);
            vbox.getChildren().add(labelDetalle);
        }

        ImageView imageWarning = new ImageView(new Image(getClass().getResource("/com/ui/img/citaPane/done.png").toExternalForm()));
        imageWarning.setFitWidth(50);
        imageWarning.setFitHeight(50);

        VBox imageContainer = new VBox();
        imageContainer.getChildren().add(imageWarning);
        vbox.getChildren().add(imageContainer);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.getDialogPane().setContent(vbox);
        alert.showAndWait();
    }

    /**
     * Este metodo valida la lista de Receta para saber si algun atributo de esta esta nullo, para que no
     * puedas procesar el informe con un campo nullo.
     * @param listaReceta
     * @return
     */
    private boolean validarCamposReceta(ObservableList<Receta> listaReceta) {
        return listaReceta.stream().anyMatch(receta ->
                receta.getNombre() == null || receta.getDosisEstandar() == null
                        || receta.getFechaInicial() == null
                        || receta.getFechaFinal() == null
                        || receta.getCantidadDosis() == null
                        || receta.getComentario() == null);
    }

    /**
     * Este metodo procesa el informe y con el booleano sabe si tiene que crearo o actualizado,
     * verifica de primeras si solo lo que quieres de la receta es el informe, que esta opcion
     * esta en FXML de cita. He tirado de if/else, supongo que se podria hacer más modular.
     * @param esNuevo
     */
    private void procesarInforme(boolean esNuevo) {
        if (soloInforme.isSelected()) {
            if (!"CORTA".equals(duracion.getValue()) && !"LARGA".equals(duracion.getValue())) {
                errorTextChoice.setVisible(true);
                return;
            }

            if (esNuevo) {
                String observacion = ObservacionCitaText.getText();
                informeCreado = citaJDBCDAO.crearConsulta(idCita, duracion.getValue(), observacion);
                if (informeCreado) {
                    citaJDBCDAO.actualizarEstado("Realizada", observacion, idCita);
                }
            } else {
                RecetaJDBCDAO recetaJDBCDAO = new RecetaJDBCDAO();
                recetaJDBCDAO.setObservacionDuracion(idCita, ObservacionMedicoText.getText(), duracion.getValue());
                citaJDBCDAO.setInforme(idCita, ObservacionCitaText.getText());
            }

            Stage stage = (Stage) errorText.getScene().getWindow();
            stage.close();
            MainPanelController.detalleCitaStage = null;
            mostrarAlerta("Receta completada", "La receta se ha completado con éxito!", "Recuerda, se puede modificar la receta en Realizadas");
        } else {
            if ((esNuevo && listaReceta.isEmpty()) || (!esNuevo && listaRecetaExistente.isEmpty())) {
                errorText.setText(esNuevo ? "No se han añadido medicamentos para recetar." : "No se han añadido medicamentos para actualizar.");
                errorText.setStyle("-fx-text-fill: red;");
                return;
            }

            boolean algunAtributoEsNull = validarCamposReceta(esNuevo ? listaReceta : listaRecetaExistente);
            if (algunAtributoEsNull) {
                errorText.setText("Completa los campos restantes.");
                errorText.setStyle("-fx-text-fill: red;");
                return;
            }

            if (!"CORTA".equals(duracion.getValue()) && !"LARGA".equals(duracion.getValue())) {
                errorTextChoice.setVisible(true);
                return;
            }

            RecetaJDBCDAO recetaJDBCDAO = new RecetaJDBCDAO();
            if (esNuevo) {
                informeCreado = citaJDBCDAO.crearConsulta(idCita, duracion.getValue(), ObservacionCitaText.getText());
                if (informeCreado) {
                    for (Receta receta : listaReceta) {
                        recetaJDBCDAO.insertarReceta(receta, idCita);
                    }
                    citaJDBCDAO.actualizarEstado("Realizada", ObservacionCitaText.getText(), idCita);
                }
            } else {
                recetaJDBCDAO.eliminarReceta(idCita);
                for (Receta receta : listaRecetaExistente) {
                    recetaJDBCDAO.insertarReceta(receta, idCita);
                }
                citaJDBCDAO.setInforme(idCita, ObservacionCitaText.getText());
                recetaJDBCDAO.setObservacionDuracion(idCita, ObservacionMedicoText.getText(), duracion.getValue());
            }

            Stage stage = (Stage) errorText.getScene().getWindow();
            stage.close();
            MainPanelController.detalleCitaStage = null;
            mostrarAlerta("Receta completada", "La receta se ha completado con éxito!", "Recuerda, se puede modificar la receta en Realizadas");
        }
    }

    /**
     * Estos dos metodos son accionados por el metodo anterior del boolean, para hacerlo más modular.
     */
    public void actualizarInforme() {
        procesarInforme(false);
    }

    public void informeNuevo() {
        procesarInforme(true);
    }

    /**
     * Aquí no he podido modularizar la alerta, entonces la he instanciado directamente ya que esta
     * es más compleja, porque te dara a escoger en la alerta la confirmación de eliminacion de cita,
     * el if del final es la que se encarca de que si le has dado a aceptar elimine la cita y te cierre la ventana.
     */
    public void eliminarInforme() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar cancelación de cita");
        alert.setHeaderText(null);

        VBox vbox = new VBox();

        Label labelNormal = new Label("¿Estás seguro de que deseas cancelar esta cita?");
        labelNormal.setTextFill(Color.RED);
        labelNormal.setStyle("-fx-font-weight: bold; -fx-font-size: 13pt;"); // Aumentar el tamaño de la letra
        vbox.getChildren().add(labelNormal);

        Label labelNegrita = new Label("¡Esta acción es irreversible!");
        labelNegrita.setStyle("-fx-font-size: 13pt;"); // Aumentar el tamaño de la letra
        labelNegrita.setTextFill(Color.RED);
        vbox.getChildren().add(labelNegrita);

        ImageView imageDanger= new ImageView(new Image(getClass().getResource("/com/ui/img/mainPane/warning.png").toExternalForm()));
        imageDanger.setFitWidth(75); // Ajusta el ancho de la imagen según tus necesidades
        imageDanger.setFitHeight(75); // Ajusta la altura de la imagen según tus necesidades

        VBox imageContainer = new VBox();
        imageContainer.getChildren().add(imageDanger);
        imageContainer.setAlignment(Pos.CENTER); // Centrar la imagen

        vbox.getChildren().add(imageContainer);

        alert.getDialogPane().setContent(vbox);

        ButtonType buttonTypeAceptar = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(buttonTypeAceptar, buttonTypeCancelar);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == buttonTypeAceptar) {
            // El usuario ha confirmado la cancelación, llamar al método para cancelar la cita
            citaJDBCDAO.canelarCita(this.idCita);
            cerrarCita();
        }
    }

    /**
     * Esta es la lista de la izquierda del panel, la cual te muestra todos los medicamentos disponibles, además por cada
     * medicamento en la base de datos, creara un boton que tiene su ID que es el de la imagen del "+" para que este accione otro metodo
     * que añadira a la lista segun sea modificada o nueva a esta.
     * @param tablaMedicamentos
     */
    @FXML
    private void showMedicamentos(ObservableList<Medicamento> tablaMedicamentos) {
        nombreMedicamento.setCellValueFactory(new PropertyValueFactory<Medicamento, String>("nombre"));
        dosisMedicamento.setCellValueFactory(new PropertyValueFactory<Medicamento, String>("dosisEstandar"));
        listaMedicamentos.setItems(tablaMedicamentos);

        añadirMedicamento.setCellFactory(new Callback<TableColumn<Medicamento, Void>, TableCell<Medicamento, Void>>() {
            @Override
            public TableCell<Medicamento, Void> call(TableColumn<Medicamento, Void> param) {
                return new TableCell<>() {
                    private final Button button = new Button();
                    {
                        // Crear un ImageView con la imagen deseada
                        ImageView imageView = new ImageView(new Image(getClass().getResource("/com/ui/img/citaPane/plus.png").toExternalForm()));

                        // Establecer el tamaño del ImageView según tus necesidades
                        imageView.setFitWidth(25);
                        imageView.setFitHeight(25);

                        // Establecer el ImageView como gráfico del botón
                        button.setGraphic(imageView);

                        // Hacer el botón transparente
                        button.setStyle("-fx-background-color: transparent;");

                        // Agregar efecto de sombra y cambiar cursor al pasar el mouse sobre el botón
                        DropShadow shadow = new DropShadow();
                        shadow.setColor(Color.GRAY); // Color de la sombra
                        shadow.setWidth(10); // Ancho de la sombra
                        shadow.setHeight(10); // Altura de la sombra

                        button.setOnMouseEntered(event -> {
                            // Cambiar el cursor cuando el mouse pasa sobre el botón
                            button.setCursor(Cursor.HAND);

                            // Agregar efecto de sombra cuando el mouse pasa sobre el botón
                            button.setEffect(shadow);
                        });

                        button.setOnMouseExited(event -> {
                            // Restaurar el cursor cuando el mouse sale del botón
                            button.setCursor(Cursor.DEFAULT);

                            // Eliminar el efecto de sombra cuando el mouse sale del botón
                            button.setEffect(null);
                        });

                        button.setOnAction(event -> {
                            Medicamento medicamento = getTableView().getItems().get(getIndex());
                            int medicamentoId = medicamento.getId();
                            addMedicamentoReceta(medicamentoId);
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

    /**
     * Este es el metodo que que se llama al mainPanelController cuando quieres modificar la receta,
     * esta función se encarga de settear todos los valores de la Receta en la base de datos en el panel.
     * Por eso iteramos la lista y añadimos a la nueva. Como se puede ver utiliza "listaRecetaEcistente".
     * Tambíen como en la otra función se crea un boton por cada elemento con su idMedicamento para eliminarla.
     *
     * @param idCita --> Setea segun la cita abierta.
     */
    public void setReceta(int idCita) {
        estado = true;
        eliminarMensaje.setVisible(true);
        eliminarCita.setVisible(true);
        generar.setText("Actualizar");
        RecetaJDBCDAO dao = new RecetaJDBCDAO();
        List<Receta> ListaRecetaCita = dao.obtenerReceta(idCita);
        for (Receta receta : ListaRecetaCita) {
            Medicamento medicamento = mapaMedicamentos.get(receta.getIdMed());
            Receta recetaExistente = new Receta(numeroReceta++, medicamento.getId(),
                    medicamento.getNombre(),
                    medicamento.getDosisEstandar(),
                    receta.getFechaInicial(),
                    receta.getFechaFinal(),
                    receta.getCantidadDosis(),
                    receta.getComentario());
            listaRecetaExistente.add(recetaExistente);
        }
        // Configurar las celdas para cada columna con CellValueFactory personalizada
        nombreLista.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        dosisEstandar.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDosisEstandar()));
        fechaInicial.setCellValueFactory(new PropertyValueFactory<>("fechaInicial"));
        fechaFinal.setCellValueFactory(new PropertyValueFactory<>("fechaFinal"));
        cantidadDosis.setCellValueFactory(new PropertyValueFactory<>("cantidadDosis"));
        comentario.setCellValueFactory(new PropertyValueFactory<>("comentario"));

        tablaReceta.setItems(listaRecetaExistente);

        // Configurar celdas editables para las columnas que deben ser editables
        setUpEditableCells();

        eliminar.setCellFactory(column -> {
            return new TableCell<Receta, Button>() {
                private final Button deleteButton = new Button();
                {
                    // Crear un ImageView con la imagen deseada
                    ImageView imageView = new ImageView(new Image(getClass().getResource("/com/ui/img/citaPane/remove.png").toExternalForm()));

                    // Establecer el tamaño del ImageView según tus necesidades
                    imageView.setFitWidth(25);
                    imageView.setFitHeight(25);

                    // Establecer el ImageView como gráfico del botón
                    deleteButton.setGraphic(imageView);

                    // Hacer el botón transparente
                    deleteButton.setStyle("-fx-background-color: transparent;");

                    // Agregar efecto de sombra y cambiar cursor al pasar el mouse sobre el botón
                    DropShadow shadow = new DropShadow();
                    shadow.setColor(Color.GRAY); // Color de la sombra
                    shadow.setWidth(5); // Ancho de la sombra
                    shadow.setHeight(5); // Altura de la sombra

                    deleteButton.setOnMouseEntered(event -> {
                        // Cambiar el cursor cuando el mouse pasa sobre el botón
                        deleteButton.setCursor(Cursor.HAND);

                        // Agregar efecto de sombra cuando el mouse pasa sobre el botón
                        deleteButton.setEffect(shadow);
                    });

                    deleteButton.setOnMouseExited(event -> {
                        // Restaurar el cursor cuando el mouse sale del botón
                        deleteButton.setCursor(Cursor.DEFAULT);

                        // Eliminar el efecto de sombra cuando el mouse sale del botón
                        deleteButton.setEffect(null);
                    });

                    deleteButton.setOnAction(event -> {
                        Receta receta = getTableRow().getItem();
                        if (receta != null) {
                            listaRecetaExistente.remove(receta);
                        }
                    });
                }

                @Override
                protected void updateItem(Button button, boolean empty) {
                    super.updateItem(button, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
        });
        getComentarios(idCita);
    }

    /**
     * Este recoge lo restante de setReceta. es decir, los comentarios y la duración y las pone
     * en el panel.
     * @param idCita --> Otra vez recoge la información segun la cita.
     */
    private void getComentarios(int idCita) {
        RecetaJDBCDAO recetaJDBCDAO = new RecetaJDBCDAO();
        CitaJDBCDAO citaJDBCDAO = new CitaJDBCDAO();
        ObservacionCitaText.setText(citaJDBCDAO.getInforme(idCita));
        ObservacionMedicoText.setText(recetaJDBCDAO.getObservacion(idCita));
        duracion.setValue(recetaJDBCDAO.getDuracion(idCita));
    }

    /**
     * Basicamente se ha modularizado el llamar a estas funciones ya que se hace varias veces.
     *
     * Aquí explicare que los setUp's de se encargan de crear celdas editables para poder actualizarlas en tiempo real al objeto
     * asi permite que no haya errores, es un poco forzado ya que si cambias cada caracter actualiza el objeto de la lista, pero es
     * la manera de no tener errores que hemos encontrado.
     */
    private void setUpEditableCells() {
        // Configurar celdas editables para las columnas que deben ser editables
        setUpDatePickers();
        setUpDosisCell();
        setUpComentarioCell();
    }

    /**
     * Esta función se encarga de añadir el medicamento seleccionado con el "+" a la lista correspondiente, sea la nueva
     * o la que se tenga que modificar. Lo mismo se crea un boton de eliminar por cada elemento. Se podria modular un poco más.
     * La receta recoge por el hasMap inicializado arriba segun el idMedicamento que contiene ese boton, para posicionar la información
     * en el objeto, los otros se inicializan en nullo para poder introducir la información en ellos.
     * @param idMedicamento
     */
    @FXML
    private void addMedicamentoReceta(int idMedicamento) {
        Medicamento medicamento = mapaMedicamentos.get(idMedicamento);
        Receta receta = new Receta(numeroReceta++,medicamento.getId(), medicamento.getNombre(), medicamento.getDosisEstandar(), null, null, null, null);

        if (estado) {
            listaRecetaExistente.add(receta);
        } else {
            listaReceta.add(receta);
        }

        nombreLista.setCellValueFactory(new PropertyValueFactory<Receta, String>("nombre"));
        dosisEstandar.setCellValueFactory(new PropertyValueFactory<Receta, String>("dosisEstandar"));

        if (estado) {
            tablaReceta.setItems(listaRecetaExistente);
        } else {
            tablaReceta.setItems(listaReceta);
        }

        setUpEditableCells();

        eliminar.setCellFactory(column -> {
            return new TableCell<Receta, Button>() {
                private final Button deleteButton = new Button();
                {
                    // Crear un ImageView con la imagen deseada
                    ImageView imageView = new ImageView(new Image(getClass().getResource("/com/ui/img/citaPane/remove.png").toExternalForm()));

                    // Establecer el tamaño del ImageView según tus necesidades
                    imageView.setFitWidth(25);
                    imageView.setFitHeight(25);

                    // Establecer el ImageView como gráfico del botón
                    deleteButton.setGraphic(imageView);

                    // Hacer el botón transparente
                    deleteButton.setStyle("-fx-background-color: transparent;");

                    // Agregar efecto de sombra y cambiar cursor al pasar el mouse sobre el botón
                    DropShadow shadow = new DropShadow();
                    shadow.setColor(Color.GRAY); // Color de la sombra
                    shadow.setWidth(5); // Ancho de la sombra
                    shadow.setHeight(5); // Altura de la sombra

                    deleteButton.setOnMouseEntered(event -> {
                        // Cambiar el cursor cuando el mouse pasa sobre el botón
                        deleteButton.setCursor(Cursor.HAND);

                        // Agregar efecto de sombra cuando el mouse pasa sobre el botón
                        deleteButton.setEffect(shadow);
                    });

                    deleteButton.setOnMouseExited(event -> {
                        // Restaurar el cursor cuando el mouse sale del botón
                        deleteButton.setCursor(Cursor.DEFAULT);

                        // Eliminar el efecto de sombra cuando el mouse sale del botón
                        deleteButton.setEffect(null);
                    });

                    deleteButton.setOnAction(event -> {
                        Receta receta = getTableRow().getItem();
                        if (receta != null) {
                            if (estado) {
                                listaRecetaExistente.remove(receta);
                            } else {
                                listaReceta.remove(receta);
                            }
                        }
                    });
                }

                @Override
                protected void updateItem(Button button, boolean empty) {
                    super.updateItem(button, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
        });

    }

    /**
     * Utiliza el DaraPicker proporcionado por JavaFX para seleccionar la fecha, aquí lo relevante, es que de primeras
     * la fecha inicial te pone por defecto la actual y además tiene una comprobación de si la fecha que introduces
     * es anterior a la actual, no te deje posicionarla y crear el informe.
     */
    @FXML
    private void setUpDatePickers() {
        fechaInicial.setCellValueFactory(new PropertyValueFactory<>("fechaInicial"));
        fechaInicial.setCellFactory(col -> new TableCell<Receta, Date>() {
            private final DatePicker datePicker = new DatePicker();

            {
                // Listener para actualizar el valor de fechaInicio en la Receta cuando cambia la fecha seleccionada
                datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                    if (getTableRow() != null) {
                        Receta receta = getTableRow().getItem();
                        receta.setFechaInicial(Date.valueOf(newValue)); // Convertir LocalDate a Date
                    }
                });
            }

            @Override
            protected void updateItem(Date date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    datePicker.setValue(date != null ? date.toLocalDate() : LocalDate.now()); // Establecer la fecha actual como valor predeterminado
                    setGraphic(datePicker);
                }
            }
        });

        fechaFinal.setCellValueFactory(new PropertyValueFactory<>("fechaFinal"));
        fechaFinal.setCellFactory(col -> new TableCell<Receta, Date>() {
            private final DatePicker datePicker = new DatePicker();

            {
                // Listener para actualizar el valor de fechaFin en la Receta cuando cambia la fecha seleccionada
                datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                    if (getTableRow() != null) {
                        Receta receta = getTableRow().getItem();

                        // Verificar si la fecha final es anterior a la fecha inicial
                        if (receta.getFechaInicial() != null && newValue != null) {
                            // Convertir java.sql.Date a LocalDate
                            LocalDate fechaInicialLocalDate = Instant.ofEpochMilli(receta.getFechaInicial().getTime())
                                    .atZone(ZoneId.systemDefault())
                                    .toLocalDate();
                            LocalDate newValueLocalDate = newValue;

                            if (newValueLocalDate.isBefore(fechaInicialLocalDate)) {
                                // La fecha final es anterior a la fecha inicial, tomar alguna acción apropiada
                                errorTextFecha.setText("La fecha final no puede ser anterior a la fecha inicial.");
                                errorTextFecha.setStyle("-fx-text-fill: red;");
                            } else {
                                errorTextFecha.setText("");
                                receta.setFechaFinal(Date.valueOf(newValueLocalDate)); // Convertir LocalDate a Date
                            }

                        }
                    }
                });
            }

            @Override
            protected void updateItem(Date date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    datePicker.setValue(date != null ? date.toLocalDate() : null);
                    setGraphic(datePicker);
                }
            }
        });
    }

    @FXML
    private void setUpDosisCell() {
        cantidadDosis.setCellValueFactory(new PropertyValueFactory<>("cantidadDosis"));
        // Límite de caracteres en el TextField
        cantidadDosis.setCellFactory(col -> new TableCell<Receta, String>() {
            private final TextField textField = new TextField();

            {
                // Listener para actualizar el valor de cantidadDosis en la Receta cuando cambia el texto
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (getTableRow() != null) {
                        Receta receta = getTableRow().getItem();
                        // Verificar si la nueva cadena excede el límite de 15 caracteres
                        if (newValue != null && newValue.length() > 15) {
                            textField.setText(oldValue); // Si excede, se restaura el valor anterior
                        } else {
                            receta.setCantidadDosis(newValue); // Si no excede, se establece el nuevo valor
                        }
                    }
                });
            }

            @Override
            protected void updateItem(String amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    textField.setText(amount != null ? amount : ""); // Se asegura de establecer una cadena vacía si amount es nulo
                    setGraphic(textField);
                }
            }
        });
    }

    @FXML
    private void setUpComentarioCell() {
        comentario.setCellValueFactory(new PropertyValueFactory<>("comentario"));
        comentario.setCellFactory(col -> new TableCell<Receta, String>() {
            private final TextField textField = new TextField();

            {
                // Listener para actualizar el valor de comentario en la Receta cuando cambia el texto
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (getTableRow() != null) {
                        Receta receta = getTableRow().getItem();
                        // Verificar si la nueva cadena excede el límite de 100 caracteres
                        if (newValue != null && newValue.length() > 100) {
                            textField.setText(oldValue); // Si excede, se restaura el valor anterior
                        } else {
                            receta.setComentario(newValue); // Si no excede, se establece el nuevo valor
                        }
                    }
                });
            }

            @Override
            protected void updateItem(String comment, boolean empty) {
                super.updateItem(comment, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    textField.setText(comment != null ? comment : ""); // Se asegura de establecer una cadena vacía si comment es nulo
                    setGraphic(textField);
                }
            }
        });
    }

    /**
     * Este metodo es el accionado por el boton "Buscar" de la lista izquierda con todos los medicamentos,
     * lo que hace es buscar por las letras y "palabraClave" dentro de la lista, para mostrarte solo los resultados
     * con las letras correspondientes. Su funcion es facilitar la busqueda de medicamentos.
     */
    @FXML
    private void busquedaMedicamentos() {
        String palabraClave = textMedicamento.getText().trim().toLowerCase();

        if (palabraClave.isEmpty()) {
            showMedicamentos(tablaMedicamentos);
        } else {
            FilteredList<Medicamento> filteredList = new FilteredList<>(tablaMedicamentos);

            filteredList.setPredicate(medicamento ->
                    medicamento.getNombre().toLowerCase().contains(palabraClave)
            );

            ObservableList<Medicamento> medicamentosFiltrados = FXCollections.observableArrayList(filteredList);
            showMedicamentos(medicamentosFiltrados);
        }
    }
    //</editor-fold>

}