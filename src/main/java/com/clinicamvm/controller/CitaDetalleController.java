package com.clinicamvm.controller;

import business.entities.Medicamento;
import business.entities.Receta;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import java.util.*;

public class CitaDetalleController implements Initializable {


    //<editor-fold defaultstate="collapsed" desc="Elementos FXML Receta">
        @FXML
        private TableView<Receta> tablaReceta;
        @FXML
        private TableColumn<Receta, String> dosisEstandar,nombreLista,cantidadDosis,comentario;
        @FXML
        private TableColumn<Receta, Date> fechaInicial, fechaFinal;
        @FXML
        private TableColumn<Receta, Button> eliminar;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Elementos FXML Medicamento">
         @FXML
        private TableColumn<Medicamento, String> nombreMedicamento,dosisMedicamento;
        @FXML
        private TableColumn<Medicamento, Void> añadirMedicamento;
        @FXML
        private TableView<Medicamento> listaMedicamentos;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Elementos FXML">
        @FXML
        private Label errorText, errorTextFecha, lblIdCita, errorTextChoice;
        @FXML
        private Button generar;
        @FXML
        private TextField textMedicamento;
        @FXML
        private TextArea motivoCitaText, ObservacionCitaText;
        @FXML
        private ChoiceBox duracion;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Elementos normales">
        private int idCita,numeroReceta;
        private boolean informeCreado = false;
        private CitaJDBCDAO citaJDBCDAO;
        private MedicamentoJDBCDAO medicamentoJDBCDAO;
        private ObservableList<Receta> listaReceta = FXCollections.observableArrayList();
        private ObservableList<Receta> listaRecetaExistente = FXCollections.observableArrayList();
        private ObservableList<Medicamento> tablaMedicamentos;
        private Map<Integer, Medicamento> mapaMedicamentos = new HashMap<>();
        private boolean estado = false;
    //</editor-fold>

    @Override //Aqui van las primeras ejecuciones cuando se inicializa
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
    }

    //<editor-fold defaultstate="collapsed" desc="Metodos de cita">

    public void setIdCita(int idCita) {
        lblIdCita.setText("Cita numero " + idCita);
        this.idCita = idCita;
    }

    public void setMotivo(int idCita) {
        motivoCitaText.setText(citaJDBCDAO.getMotivo(idCita));
    }

    @FXML
    public void crearInforme() {
        if (estado) {
            actualizarInforme();
        } else {
            informeNuevo();
        }

    }

    public void actualizarInforme() {
        for (Receta receta : listaRecetaExistente) {
            RecetaJDBCDAO recetaJDBCDAO = new RecetaJDBCDAO();
            recetaJDBCDAO.actualizarReceta(receta,this.idCita);
        }
        errorText.setText("Receta actualizada exitosamente.");
        errorText.setStyle("-fx-text-fill: green;");
    }

    public void informeNuevo() {
        boolean algunAtributoEsNull = listaReceta.stream()
                .anyMatch(receta -> receta.getNombre() == null
                        || receta.getDosisEstandar() == null
                        || receta.getFechaInicial() == null
                        || receta.getFechaFinal() == null
                        || receta.getCantidadDosis() == null
                        || receta.getComentario() == null);

        if (algunAtributoEsNull) {
            errorText.setText("Completa los campos restantes.");
            errorText.setStyle("-fx-text-fill: red;");
        } else if (!"CORTA".equals(duracion.getValue()) && !"LARGA".equals(duracion.getValue())) {
            errorTextChoice.setVisible(true);
        }else {
            errorTextChoice.setVisible(false);
            errorText.setText("Receta creada exitosamente.");
            errorText.setStyle("-fx-text-fill: green;");

            informeCreado = citaJDBCDAO.crearConsulta(idCita, String.valueOf(duracion.getValue()));

            String observacion = ObservacionCitaText.getText();


            if (informeCreado == true){
                for (Receta receta : listaReceta) {
                    RecetaJDBCDAO recetaJDBCDAO = new RecetaJDBCDAO();
                    recetaJDBCDAO.insertarReceta(receta,this.idCita);
                }
                citaJDBCDAO.actualizarEstado("Realizada",observacion,idCita);
            }
        }
    }


    @FXML
    private void showMedicamentos(ObservableList<Medicamento> tablaMedicamentos) {
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

    public void setReceta(int idCita) {
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

        // Configurar celdas editables para las columnas que deben ser editables
        setUpEditableCells();

        eliminar.setCellFactory(column -> {
            return new TableCell<Receta, Button>() {
                private final Button deleteButton = new Button("-");
                {
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

        // Establecer los valores manualmente a la tabla
        tablaReceta.setItems(listaRecetaExistente);
        estado = true;
    }

    private void setUpEditableCells() {
        // Configurar celdas editables para las columnas que deben ser editables
        setUpDatePickers();
        setUpDosisCell();
        setUpComentarioCell();
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
        estado = false;
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