package persistence.utils;


import business.entities.Medicamento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCUtils {

    public static Connection dbLink;

    public static Connection getConnection() {
        String databaseName = "adminmontalban_clinica";
        String databaseUser = "329292_admin";
        String databasePassword = "adminmontalban!";
        String url = "jdbc:mysql://mysql-adminmontalban.alwaysdata.net:3306/adminmontalban_clinica";


        try {

            Class.forName("com.mysql.cj.jdbc.Driver");
            dbLink = DriverManager.getConnection(url, databaseUser, databasePassword);

        } catch (Exception e) {

            e.printStackTrace();

        }

        return dbLink;

    }


    public ObservableList<Medicamento> buscarTextoEnMedicamentos(String texto, ObservableList<Medicamento> listaMedicamentos) {
        ObservableList<Medicamento> medicamentosCoincidentes = FXCollections.observableArrayList();

        for (Medicamento medicamento : listaMedicamentos) {
            if (medicamento.getNombre().toLowerCase().contains(texto.toLowerCase()) ||
                    medicamento.getDescripcion().toLowerCase().contains(texto.toLowerCase())) {
                medicamentosCoincidentes.add(medicamento);
            }
        }

        return medicamentosCoincidentes;
    }

















}
