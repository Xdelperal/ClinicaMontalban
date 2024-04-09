package persistence.daos.impl;

import business.entities.Cita;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import persistence.daos.contracts.CitaDAO;
import persistence.utils.*;
import java.sql.*;
import java.util.Date;

public class CitaJDBCDAO implements CitaDAO {

    private String userNameLabel = "";

    @FXML
    private TableView pendientes;

    private SQLQueries sqlQueries;

    ObservableList<Cita> citas = FXCollections.emptyObservableList();

    public CitaJDBCDAO(Connection connection) {

        this.sqlQueries = new SQLQueries();
        this.citas = FXCollections.observableArrayList(); // Inicializa citas como una nueva ObservableList

    }


    @Override
    public ObservableList<Cita> obtenerLista(String estadoCita, String userName) {
        try {
            // Establecer la conexión a la base de datos
            Connection connection = JDBCUtils.getConnection();

            // Consulta SQL para obtener el ID del trabajador
            String sql = "SELECT idTrabajador FROM personal WHERE DNI = ?";

            try {
                // Preparar la declaración SQL
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, userName);

                // Ejecutar la consulta
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {

                    // Consulta SQL para obtener las citas pendientes con el nombre del cliente
                    String sqlCitas = sqlQueries.getCitas();


                    // Preparar la declaración SQL para la segunda consulta
                    PreparedStatement statementCitas = connection.prepareStatement(sqlCitas);
                    statementCitas.setString(1, userName);
                    statementCitas.setString(2,estadoCita);

                    // Ejecutar la segunda consulta
                    ResultSet resultSetCitas = statementCitas.executeQuery();

                    // Recorrer el resultado de la consulta
                    while (resultSetCitas.next()) {
                        // Obtener los valores de las columnas
                        int idCita = resultSetCitas.getInt("idCita");
                        int idCliente = resultSetCitas.getInt("idCliente");
                        String nombre = resultSetCitas.getString("nombre");
                        String estado = resultSetCitas.getString("estado");
                        Date fecha = resultSetCitas.getDate("fecha");
                        Time hora = resultSetCitas.getTime("hora");
                        String descripcion = resultSetCitas.getString("descripcion");

                        // Crear un objeto Cita con los datos obtenidos
                        Cita nuevaCita = new Cita(idCita, idCliente, nombre, estado, (java.sql.Date) fecha, hora, descripcion);
                        citas.add(nuevaCita);
                    }
                }


                // Cerrar recursos
                resultSet.close();
                statement.close();
                connection.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


        return citas;
    }

    @Override
    public ObservableList<Cita> buscar(String dni) {

        if (citas.size()>0){
            citas.clear();
        }

        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement statementCitas = connection.prepareStatement("SELECT cita.idCita, cita.idCliente, persona.nombre, cita.estado, cita.fecha, cita.hora, cita.descripcion " +
                     "FROM cita " +
                     "INNER JOIN cliente ON cita.idCliente = cliente.idCliente " +
                     "INNER JOIN persona ON cliente.DNI = persona.DNI " +
                     "WHERE cliente.DNI = ?")) {

            statementCitas.setString(1, dni);
            ResultSet resultSetCitas = statementCitas.executeQuery();

            while (resultSetCitas.next()) {
                int idCita = resultSetCitas.getInt("idCita");
                int idCliente = resultSetCitas.getInt("idCliente");
                String nombre = resultSetCitas.getString("nombre");
                String estado = resultSetCitas.getString("estado");
                Date fecha = resultSetCitas.getDate("fecha");
                java.sql.Time hora = resultSetCitas.getTime("hora");
                String descripcion = resultSetCitas.getString("descripcion");
                Cita nuevaCita = new Cita(idCita, idCliente, nombre, estado, (java.sql.Date) fecha, hora, descripcion);
                citas.add(nuevaCita);
                // System.out.println(nuevaCita.getDatos());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  citas;
    }


}