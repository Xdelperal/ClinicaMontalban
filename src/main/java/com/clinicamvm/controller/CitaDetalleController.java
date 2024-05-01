package com.clinicamvm.controller;

import business.entities.Medicamento;
import business.entities.Receta;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class CitaDetalleController implements Initializable {

    @FXML
    private TableView<Medicamento> listaMedicamentos;

    @FXML
    private TableView<Receta> tablaReceta;

    @FXML
    private TableColumn<Receta, String> dosisEstandar,nombreLista,cantidadDosis,comentario;
    @FXML
    private TableColumn<Receta, Date> fechaInicial, fechaFinal;

    @FXML
    private TableColumn<Receta, Button> eliminar;

    @FXML
    private TableColumn<Medicamento, String> nombreMedicamento,dosisMedicamento;
    @FXML
    private TableColumn<Medicamento, Void> añadirMedicamento;

    @FXML
    private Label errorText;

    @FXML
    private Label lblIdCita;

    @FXML
    private Button buttonSearchMedicamentos;

    @FXML
    private TextField textMedicamento;

    @FXML
    private TextArea motivoCitaText, ObservacionCitaText;

    @FXML
    private Button generar;

    @FXML
    private Label errorTextFecha;

    private int idCita;
    private int numeroReceta;

    private CitaJDBCDAO citaJDBCDAO;
    private MedicamentoJDBCDAO medicamentoJDBCDAO;

    private RecetaJDBCDAO recetaJDBCDAO;

    private ObservableList<Receta> listaReceta = FXCollections.observableArrayList();
    private ObservableList<Medicamento> tablaMedicamentos;
    private Map<Integer, Medicamento> mapaMedicamentos = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        medicamentoJDBCDAO = new MedicamentoJDBCDAO();
        Connection connection = JDBCUtils.getConnection();
        citaJDBCDAO = new CitaJDBCDAO(connection);
        generar.setOnAction(event -> crearInforme());
        buttonSearchMedicamentos.setOnAction(event -> busquedaMedicamentos());
        tablaMedicamentos = medicamentoJDBCDAO.getMedicamentos();
        showMedicamentos();


        for (Medicamento medicamento : tablaMedicamentos) {
            mapaMedicamentos.put(medicamento.getId(), medicamento);
        }
    }


    public void setIdCita(int idCita) {
        lblIdCita.setText("Cita numero " + idCita);
        this.idCita = idCita;
    }

    public void setMotivo(int idCita) {
        motivoCitaText.setText(citaJDBCDAO.getMotivo(idCita));
    }

    @FXML
    public void crearInforme() {
        // Imprimir el objeto por consola
        System.out.println("Lista de Recetas:");
        for (Receta r : listaReceta) {
            System.out.println(r);
        }

        boolean algunAtributoEsNull = listaReceta.stream()
                .anyMatch(receta -> receta.getNombre() == null
                        || receta.getDosisEstandar() == null
                        || receta.getFechaInicial() == null
                        || receta.getFechaFinal() == null
                        || receta.getCantidadDosis() == null
                        || receta.getComentario() == null);

        if (algunAtributoEsNull) {
            errorText.setText("Completa los campos restantes.");
        } else {
            errorText.setText("Receta creada exitosamente.");
            errorText.setStyle("-fx-text-fill: green;");

            String observacion = ObservacionCitaText.getText();
            // Suponiendo que listaReceta es una lista de objetos Receta
            for (Receta receta : listaReceta) {
                // Suponiendo que tienes un método en tu DAO para insertar una receta
                RecetaJDBCDAO recetaJDBCDAO1 = new RecetaJDBCDAO();
                recetaJDBCDAO1.insertarReceta(receta,this.idCita);
            }
        }
    }

    @FXML
    private void showMedicamentos() {
        nombreMedicamento.setCellValueFactory(new PropertyValueFactory<Medicamento, String>("nombre"));
        dosisMedicamento.setCellValueFactory(new PropertyValueFactory<Medicamento, String>("dosisEstandar"));
        listaMedicamentos.setItems(tablaMedicamentos);

        añadirMedicamento.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Medicamento, Void> call(TableColumn<Medicamento, Void> param) {
                return new TableCell<>() {
                    private final Button button = new Button("+");

                    {
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

    @FXML
    private void addMedicamentoReceta(int idMedicamento) {
        Medicamento medicamento = mapaMedicamentos.get(idMedicamento);
        Receta receta = new Receta(numeroReceta++,medicamento.getId(), medicamento.getNombre(), medicamento.getDosisEstandar(), null, null, null, null);

        listaReceta.add(receta);

        nombreLista.setCellValueFactory(new PropertyValueFactory<Receta, String>("nombre"));
        dosisEstandar.setCellValueFactory(new PropertyValueFactory<Receta, String>("dosisEstandar"));
        tablaReceta.setItems(listaReceta);

        setUpDatePickers();
        setUpDosisCell();
        setUpComentarioCell();

        eliminar.setCellFactory(column -> {
            return new TableCell<Receta, Button>() {
                private final Button deleteButton = new Button("-");
                {
                    deleteButton.setOnAction(event -> {
                        Receta receta = getTableRow().getItem();
                        if (receta != null) {
                            listaReceta.remove(receta);
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
        cantidadDosis.setCellFactory(col -> new TableCell<Receta, String>() {
            private final TextField textField = new TextField();

            {
                // Listener para actualizar el valor de comentario en la Receta cuando cambia el texto
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (getTableRow() != null) {
                        Receta receta = getTableRow().getItem();
                        receta.setCantidadDosis(newValue);
                    }
                });
            }

            @Override
            protected void updateItem(String comment, boolean empty) {
                super.updateItem(comment, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    textField.setText(comment);
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
                        receta.setComentario(newValue);
                    }
                });
            }

            @Override
            protected void updateItem(String comment, boolean empty) {
                super.updateItem(comment, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    textField.setText(comment);
                    setGraphic(textField);
                }
            }
        });
    }

    @FXML
    private void busquedaMedicamentos() {
        ObservableList<Medicamento> medicamentosBusqueda = medicamentoJDBCDAO.buscar(textMedicamento.getText());
        listaMedicamentos.setItems(medicamentosBusqueda);
    }







}