package com.clinicamvm.controller;

import business.entities.Medicamento;
import business.entities.Receta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import persistence.daos.impl.CitaJDBCDAO;
import persistence.daos.impl.MedicamentoJDBCDAO;
import persistence.utils.*;
import java.time.LocalDate;

import java.net.URL;
import java.sql.Connection;
import java.util.*;

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
    private MedicamentoJDBCDAO medicamentoJDBCDAO;

    @FXML
    private Button buttonSearchMedicamentos;

    @FXML
    private TextField textMedicamento;

    @FXML
    private TextArea motivoCitaText,ObservacionCitaText;
    @FXML
    private Button generar;

    @FXML
    private DatePicker fechaInicio;

    private int idCita;

    private int numeroReceta;

    private SQLQueries sqlQueries;
    private CitaJDBCDAO citaJDBCDAO;

   // private List<Receta> tablaReceta = new ArrayList<>();;
    private ObservableList<Receta> listaReceta = FXCollections.observableArrayList();
    private ObservableList<Medicamento> tablaMedicamentos;
    JDBCUtils recursos = new JDBCUtils();
    // Otros elementos de la ventana y métodos necesarios

    // Mapa para mapear los medicamentos por su id
    private Map<Integer, Medicamento> mapaMedicamentos = new HashMap<>();
    private Map<String, Receta> mapaRecetas = new HashMap<>();
    private Map<Integer, Receta> mapaRecetasInt = new HashMap<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        medicamentoJDBCDAO = new MedicamentoJDBCDAO();
        listaMedicamentos.setVisible(true);
        Connection connection = JDBCUtils.getConnection();
        citaJDBCDAO = new CitaJDBCDAO(connection);
        generar.setOnAction(event -> crearInforme());
        buttonSearchMedicamentos.setOnAction(event -> busquedaMedicamentos());
        tablaMedicamentos = medicamentoJDBCDAO.getMedicamentos();
        showMedicamentos();

        // Llenar el mapa con los medicamentos
        for (Medicamento medicamento : tablaMedicamentos) {
            mapaMedicamentos.put(medicamento.getId(), medicamento);
        }

    }

    @FXML
    private void getBusqueda(){
        listaMedicamentos.getItems().clear();
        ObservableList<Medicamento> buscarLista = medicamentoJDBCDAO.buscar(textMedicamento.getText());
        listaMedicamentos.setItems(buscarLista);

    }


    public void setIdCita(int idCita) {
        // Aquí puedes usar el ID de la cita para cargar y mostrar la información correspondiente en la ventana
        lblIdCita.setText("Cita numero " + idCita);
        this.idCita = idCita;
    }
    public void setMotivo(int idCita) {
        // Aquí puedes usar el ID de la cita para cargar y mostrar la información correspondiente en la ventana
        motivoCitaText.setText(citaJDBCDAO.getMotivo(idCita));
    }

    @FXML
    public void crearInforme(){
        citaJDBCDAO.crearInforme(this.idCita,ObservacionCitaText.getText());
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

        // Buscar el medicamento en el mapa utilizando su id como clave
        Medicamento medicamento = mapaMedicamentos.get(idMedicamento);

        // Crear un objeto Receta con el nombre y la dosis estándar del medicamento
        listaReceta.add(new Receta(numeroReceta++, medicamento.getNombre(), medicamento.getDosisEstandar(), null, null, null, null));

        nombreLista.setCellValueFactory(new PropertyValueFactory<Receta, String>("nombre"));
        dosisEstandar.setCellValueFactory(new PropertyValueFactory<Receta, String>("dosisEstandar"));

        tablaReceta.setItems(listaReceta);

        for (Receta receta : listaReceta) {
            mapaRecetas.put(medicamento.getNombre(), receta);
        }

        Receta receta = mapaRecetas.get(medicamento.getNombre());

        System.out.println("Receta encontrada: " + receta.toString());

        fechaInicial.setCellFactory(column -> {
            return new TableCell<Receta, Date>() {
                private final DatePicker datePicker = new DatePicker();

                {
                    // Configura el comportamiento del DatePicker
                    datePicker.setOnAction(event -> {
                        commitEdit(java.sql.Date.valueOf(datePicker.getValue()));
                    });
                }

                @Override
                protected void updateItem(Date fecha, boolean empty) {
                    super.updateItem(fecha, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        datePicker.setValue(LocalDate.now());
                        setGraphic(datePicker);
                    }
                }

                @Override
                public void startEdit() {
                    super.startEdit();
                    datePicker.setValue(LocalDate.now());
                    setGraphic(datePicker);
                }

                @Override
                public void cancelEdit() {
                    super.cancelEdit();
                    setGraphic(null);
                }
            };
        });

        fechaFinal.setCellFactory(column -> {
            return new TableCell<Receta, Date>() {
                private final DatePicker datePicker = new DatePicker();

                {
                    // Configura el comportamiento del DatePicker
                    datePicker.setOnAction(event -> {
                        commitEdit(java.sql.Date.valueOf(datePicker.getValue()));
                    });
                }

                @Override
                protected void updateItem(Date fecha, boolean empty) {
                    super.updateItem(fecha, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(datePicker);
                    }
                }

                @Override
                public void startEdit() {
                    super.startEdit();
                    setGraphic(datePicker);
                }

                @Override
                public void cancelEdit() {
                    super.cancelEdit();
                    setGraphic(null);
                }
            };
        });

        cantidadDosis.setCellFactory(column -> {
            return new TableCell<Receta, String>() {
                private final TextField textField = new TextField();

                {
                    textField.setOnAction(event -> {
                        commitEdit(textField.getText());
                    });
                }

                @Override
                protected void updateItem(String cantidad, boolean empty) {
                    super.updateItem(cantidad, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        textField.setText(cantidad);
                        setGraphic(textField);
                    }
                }

                @Override
                public void startEdit() {
                    super.startEdit();
                    textField.setText(getItem());
                    setGraphic(textField);
                }

                @Override
                public void cancelEdit() {
                    super.cancelEdit();
                    setGraphic(null);
                }
            };
        });

        // Primero conseguir actualizar campo de texto comentario.
        comentario.setCellFactory(column -> {
            return new TableCell<Receta, String>() {
                private final TextField textField = new TextField();

                {
                    // Configura el comportamiento del TextField
                    textField.setOnAction(event -> {
                        if (receta != null) {
                            receta.setComentario(textField.getText());
                            //commitEdit(textField.getText());
                            System.out.println("Receta actualizada: " + receta.toString());
                        }
                    });
                }

                @Override
                protected void updateItem(String comentario, boolean empty) {
                    super.updateItem(comentario, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        textField.setText(comentario);
                        setGraphic(textField);
                    }
                }

                @Override
                public void startEdit() {
                    super.startEdit();
                    // Obtén la receta actual
                    Receta receta = getTableRow().getItem();
                    if (receta != null) {
                        System.out.println("Receta a editar: " + receta.toString());
                    }
                    textField.setText(getItem());
                    setGraphic(textField);
                }

                @Override
                public void cancelEdit() {
                    super.cancelEdit();
                    setGraphic(null);
                }
            };
        });


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

        for (Receta receta2 : listaReceta) {
            System.out.println("Lista de receta entera actualizada" + receta2.toString());
        }
    }


    @FXML
    private void busquedaMedicamentos() {
        ObservableList<Medicamento> medicamentosBusqueda = recursos.buscarTextoEnMedicamentos(textMedicamento.getText(), tablaMedicamentos);
        // Agregar los elementos obtenidos a la TableView
        listaMedicamentos.setItems(medicamentosBusqueda);
    }

}
