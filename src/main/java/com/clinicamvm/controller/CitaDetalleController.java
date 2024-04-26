package com.clinicamvm.controller;

import business.entities.Cita;
import business.entities.Medicamento;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import persistence.daos.impl.CitaJDBCDAO;
import persistence.daos.impl.MedicamentoJDBCDAO;
import persistence.utils.*;
import javax.swing.*;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import java.time.LocalDate;

public class CitaDetalleController implements Initializable {

    @FXML
    private TableView<Medicamento> listaMedicamentos, listaReceta;

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

    private SQLQueries sqlQueries;
    private CitaJDBCDAO citaJDBCDAO;

    private ObservableList<Medicamento> tablaMedicamentos;

    // Otros elementos de la ventana y métodos necesarios


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

                            System.out.println("Medicamento nombre: " + medicamentoId);
                            //listaReceta.setItems(medicamentoId);

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

    @FXML
    private void busquedaMedicamentos() {
        ObservableList<Medicamento> medicamentosBusqueda = recursos.buscarTextoEnMedicamentos(textMedicamento.getText(), tablaMedicamentos);
        // Agregar los elementos obtenidos a la TableView
        listaMedicamentos.setItems(medicamentosBusqueda);
    }

}
