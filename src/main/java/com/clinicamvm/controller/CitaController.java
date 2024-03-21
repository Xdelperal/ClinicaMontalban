package com.clinicamvm.controller;

import business.entities.Cita;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import persistence.utils.JDBCUtils;
import java.sql.*;

public class CitaController {
    @FXML
    private TableView<Cita> tablaPendientes;

    private String userName;

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void getPendiente() {
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
                    String sqlCitas = "SELECT cita.idCita, cita.idCliente, persona.nombre, cita.estado, cita.fecha, cita.hora, cita.descripcion " +
                            "FROM cita " +
                            "INNER JOIN cliente ON cita.idCliente = cliente.idCliente " +
                            "INNER JOIN persona ON cliente.DNI = persona.DNI " +
                            "INNER JOIN personal ON personal.idTrabajador = cita.idTrabajador " +
                            "WHERE personal.DNI = ? AND cita.estado = 'Pendiente'";

                    // Preparar la declaración SQL para la segunda consulta
                    PreparedStatement statementCitas = connection.prepareStatement(sqlCitas);
                    statementCitas.setString(1, userName);

                    // Ejecutar la segunda consulta
                    ResultSet resultSetCitas = statementCitas.executeQuery();

                    // Procesar el resultado

                    // Hacer lo que necesites con los datos de la cita
                    tablaPendientes.getItems().clear();

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
                        Cita nuevaCita = new Cita(idCita, idCliente, nombre, estado, fecha, hora, descripcion);

                        // Añadir la cita a la tabla
                        tablaPendientes.getItems().add(nuevaCita);
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
    }
}
