package persistence.daos.impl;

import business.entities.Cita;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import persistence.daos.contracts.CitaDAO;
import persistence.utils.*;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class CitaJDBCDAO implements CitaDAO {

    @FXML
    private TableView pendientes;

    private SQLQueries sqlQueries;

    private String descripcion, TSI;

    ObservableList<Cita> citas = FXCollections.emptyObservableList();

    public CitaJDBCDAO(Connection connection) {
        this.sqlQueries = new SQLQueries();
        this.citas = FXCollections.observableArrayList();
    }

	
    public Cita getCita( String userName, int idCita1) {
        Cita cita = null;
        try {
            // Establecer la conexión a la base de datos
            Connection connection = JDBCUtils.getConnection();

            // Consulta SQL para obtener el ID del trabajador
            String sql = "SELECT idTrabajador FROM personal WHERE DNI = ?";

            // Preparar la declaración SQL
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, userName);

            // Ejecutar la consulta
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                // Consulta SQL para obtener las citas pendientes con el nombre del cliente
                String sqlCitas = sqlQueries.getCita();

                // Preparar la declaración SQL para la segunda consulta
                PreparedStatement statementCitas = connection.prepareStatement(sqlCitas);
                statementCitas.setString(1, userName);
                statementCitas.setInt(2, idCita1);

                // Ejecutar la segunda consulta
                ResultSet resultSetCitas = statementCitas.executeQuery();

                // Recorrer el resultado de la consulta
                if (resultSetCitas.next()) {
                    // Obtener los valores de las columnas
                    int idCita = resultSetCitas.getInt("idCita");
                    int idCliente = resultSetCitas.getInt("idCliente");
                    String DNI = resultSetCitas.getString("DNI");
                    String nombre = resultSetCitas.getString("nombre");
                    String estado = resultSetCitas.getString("estado");
                    Date fecha = resultSetCitas.getDate("fecha");
                    Time hora = resultSetCitas.getTime("hora");
                    String descripcion = resultSetCitas.getString("descripcion");

                    // Crear un objeto Cita con los datos obtenidos
                    cita = new Cita(idCliente, idCita, DNI, nombre, estado, (java.sql.Date) fecha, hora, descripcion);
                }

                // Cerrar recursos
                resultSetCitas.close();
                statementCitas.close();
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return cita;
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
                    statementCitas.setString(2, estadoCita);

                    // Ejecutar la segunda consulta
                    ResultSet resultSetCitas = statementCitas.executeQuery();

                    // Recorrer el resultado de la consulta
                    while (resultSetCitas.next()) {
                        // Obtener los valores de las columnas
                        int idCita = resultSetCitas.getInt("idCita");
                        int idCliente = resultSetCitas.getInt("idCliente");
                        String DNI = resultSetCitas.getString("DNI");
                        String nombre = resultSetCitas.getString("nombre");
                        String estado = resultSetCitas.getString("estado");
                        Date fecha = resultSetCitas.getDate("fecha");
                        Time hora = resultSetCitas.getTime("hora");
                        String descripcion = resultSetCitas.getString("descripcion");

                        // Crear un objeto Cita con los datos obtenidos
                        Cita nuevaCita = new Cita(idCliente, idCita, DNI,  nombre, estado, (java.sql.Date) fecha, hora, descripcion);
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
        ObservableList<Cita> citas = FXCollections.observableArrayList(); // Crear una nueva lista

        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement statementCitas = connection.prepareStatement("SELECT cita.idCita, cita.idCliente, persona.DNI, CONCAT(persona.nombre, ' ', persona.apellido) as nombreCompleto, cita.estado, cita.fecha, cita.hora, cita.descripcion " +
                     "FROM cita " +
                     "INNER JOIN cliente ON cita.idCliente = cliente.idCliente " +
                     "INNER JOIN persona ON cliente.DNI = persona.DNI " +
                     "WHERE persona.DNI = ?")) { // Cambiar la columna a persona.DNI
            statementCitas.setString(1, dni);
            ResultSet resultSetCitas = statementCitas.executeQuery();

            while (resultSetCitas.next()) {
                int idCita = resultSetCitas.getInt("idCita");
                int idCliente = resultSetCitas.getInt("idCliente");
                String DNI = resultSetCitas.getString("DNI");
                String nombreCompleto = resultSetCitas.getString("nombreCompleto"); // Cambiar al alias nombreCompleto
                String estado = resultSetCitas.getString("estado");
                Date fecha = resultSetCitas.getDate("fecha");
                Time hora = resultSetCitas.getTime("hora");
                String descripcion = resultSetCitas.getString("descripcion");
                Cita nuevaCita = new Cita(idCliente, idCita, DNI, nombreCompleto, estado, (java.sql.Date) fecha, hora, descripcion); // Usar nombreCompleto
                citas.add(nuevaCita);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return citas;
    }

    @Override
    public boolean crearConsulta(int idCita, String duracion) {
        Connection connection = JDBCUtils.getConnection();
        try {
            SQLQueries sqlQueries = new SQLQueries();
            String sqlConsulta = sqlQueries.getConsulta();
            PreparedStatement statementGetConsulta = connection.prepareStatement(sqlConsulta);
            statementGetConsulta.setInt(1, idCita);
            ResultSet resultSetCitas = statementGetConsulta.executeQuery();
             if (resultSetCitas.next()){
                 return true;
             } else{
                 LocalDate variable = LocalDate.now();

                 // Formatear la fecha en el formato DDMMYY
                 DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
                 String fechaActual = variable.format(formatter);

                 ClienteJDBCDAO clienteJDBCDAO= new ClienteJDBCDAO();
                 TSI = clienteJDBCDAO.getTsiByIdcita(idCita);
                 TSI = TSI.substring(4);
                 String codigoBarras = fechaActual + TSI  + idCita;

                 sqlConsulta = sqlQueries.                                                                                                                                        setConsulta();

                 PreparedStatement statementConsulta = connection.prepareStatement(sqlConsulta);

                 statementConsulta.setInt(1, idCita);
                 statementConsulta.setString(2, duracion);
                 statementConsulta.setString(3,codigoBarras);


                 statementConsulta.execute();
                 return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public String getMotivo(int idCita) {

        try {
            // Establecer la conexión a la base de datos
            Connection connection = JDBCUtils.getConnection();

            PreparedStatement statementMotivo = connection.prepareStatement(sqlQueries.getMotivo());
            statementMotivo.setString(1, String.valueOf(idCita));

            // Ejecutar la consulta
            ResultSet resultSetCitas = statementMotivo.executeQuery();

            // Verificar si hay resultados
            if (resultSetCitas.next()) {
                this.descripcion = resultSetCitas.getString("descripcion");
            }

            // Cerrar recursos
            resultSetCitas.close();
            statementMotivo.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar la excepción o lanzarla nuevamente si es necesario
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return descripcion;
    }


    public void actualizarEstado(String estado , String obv, int idCita){

        try {
            // Establecer la conexión a la base de datos
            Connection connection = JDBCUtils.getConnection();
            {

                // Consulta SQL para obtener las citas pendientes con el nombre del cliente
                String sqlInforme = sqlQueries.updateEstado();


                // Preparar la declaración SQL para la segunda consulta
                PreparedStatement statementInforme = connection.prepareStatement(sqlInforme);

                statementInforme.setString(1, estado);

                statementInforme.setString(2, obv);

                statementInforme.setString(3, String.valueOf(idCita));

                // Ejecutar la segunda consulta
                statementInforme.executeUpdate();


            }


            // Cerrar recursos

            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }











}
