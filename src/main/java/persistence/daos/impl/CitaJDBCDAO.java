package persistence.daos.impl;

import business.entities.Cita;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import persistence.daos.contracts.CitaDAO;
import persistence.utils.JDBCUtils;

import java.sql.*;


public class CitaJDBCDAO implements CitaDAO {


    ObservableList<Cita> citas = FXCollections.observableArrayList();

    private final Connection connection;
    private final String userNameLabel;

    @Override
    public ObservableList<Cita> obtenerLista() {

        try {
            // Establecer la conexión a la base de datos
            Connection connection = JDBCUtils.getConnection();

            // Consulta SQL para obtener el ID del trabajador
            String sql = "SELECT idTrabajador FROM personal WHERE DNI = ?";

            try {
                // Preparar la declaración SQL
                PreparedStatement statement = connection.prepareStatement(sql);
                //statement.setString(1, userNameLabel.getText());
                statement.setString(1, userNameLabel);

                // Ejecutar la consulta
                ResultSet resultSet = statement.executeQuery();

                Date dia = new Date(2003,12,13);
                citas.add(new Cita(1, 1, "seku", "Pendiente", dia, Time.valueOf("15:00:00"), "Hola que tal"));

                if (resultSet.next()) {

                    // Consulta SQL para obtener las citas pendientes con el nombre del cliente
                    String sqlCitas = "SELECT cita.idCita, personal.idCliente, persona.nombre, cita.estado, cita.fecha, cita.hora, cita.descripcion " +
                            "FROM cita " +
                            "INNER JOIN cliente ON cita.idCliente = cliente.idCliente " +
                            "INNER JOIN persona ON cliente.DNI = persona.DNI " +
                            "INNER JOIN personal ON personal.idTrabajador = cita.idTrabajador " +
                            "WHERE personal.DNI = ?";


                    // Preparar la declaración SQL para la segunda consulta
                    PreparedStatement statementCitas = connection.prepareStatement(sqlCitas);
                    statementCitas.setString(1, userNameLabel);

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
                        // Añadir la cita a la tabla

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


    public CitaJDBCDAO(Connection connection, String userName) {

            this.connection = connection;
            this.userNameLabel = userName;
    }
}
