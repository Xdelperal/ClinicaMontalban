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
import persistence.utils.JDBCUtils;

import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class CitaDetalleController implements Initializable {

    @FXML
    private TableView<Medicamento> listaMedicamentos;

    @FXML
    private TableView<Receta> tablaReceta;

    @FXML
    private TableColumn<Receta, String> nombreLista;
    @FXML
    private TableColumn<Receta, String> dosisEstandar;
    @FXML
    private TableColumn<Receta, Date> fechaInicial;
    @FXML
    private TableColumn<Receta, Date> fechaFinal;
    @FXML
    private TableColumn<Receta, String> cantidadDosis;
    @FXML
    private TableColumn<Receta, String> comentario;
    @FXML
    private TableColumn<Receta, Button> eliminar;

    @FXML
    private TableColumn<Medicamento, String> nombreMedicamento;
    @FXML
    private TableColumn<Medicamento, String> dosisMedicamento;
    @FXML
    private TableColumn<Medicamento, Void> añadirMedicamento;

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
    private DatePicker fechaInicio;

    private int idCita;
    private int numeroReceta;

    private CitaJDBCDAO citaJDBCDAO;
    private MedicamentoJDBCDAO medicamentoJDBCDAO;

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

    @FXML
    private void getBusqueda() {
        listaMedicamentos.getItems().clear();
        ObservableList<Medicamento> buscarLista = medicamentoJDBCDAO.buscar(textMedicamento.getText());
        listaMedicamentos.setItems(buscarLista);
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
        citaJDBCDAO.crearInforme(this.idCita, ObservacionCitaText.getText());
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
        Receta receta = new Receta(numeroReceta++, medicamento.getNombre(), medicamento.getDosisEstandar(), null, null, null, null);

        // Imprimir el objeto por consola
        System.out.println("Lista de Recetas:");
        for (Receta r : listaReceta) {
            System.out.println(r);
        }

        listaReceta.add(receta);

        nombreLista.setCellValueFactory(new PropertyValueFactory<Receta, String>("nombre"));
        dosisEstandar.setCellValueFactory(new PropertyValueFactory<Receta, String>("dosisEstandar"));
        tablaReceta.setItems(listaReceta);

        // Configurar celdas para fecha inicial y final
        setUpDatePickers();

        // Configurar celdas para comentario
        setUpComentarioCell();
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
                        receta.setFechaInicial(Date.valueOf(newValue));
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

        fechaFinal.setCellValueFactory(new PropertyValueFactory<>("fechaFinal"));
        fechaFinal.setCellFactory(col -> new TableCell<Receta, Date>() {
            private final DatePicker datePicker = new DatePicker();

            {
                // Listener para actualizar el valor de fechaFin en la Receta cuando cambia la fecha seleccionada
                datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
                    if (getTableRow() != null) {
                        Receta receta = getTableRow().getItem();
                        receta.setFechaFinal(Date.valueOf(newValue));
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
