package persistence.daos.impl;

import business.entities.Receta;
import persistence.daos.contracts.RecetaDAO;
import persistence.utils.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecetaJDBCDAO implements RecetaDAO {

    private SQLQueries sqlQueries;

    public RecetaJDBCDAO() {
        this.sqlQueries = new SQLQueries();
    }

    private String observacion, duracion;

    public void insertarReceta(Receta receta, int idCita) {
        try (Connection connection = JDBCUtils.getConnection()) {
            // Iniciar una transacción
            connection.setAutoCommit(false);

            try {
                // Insertar la nueva receta
                SQLQueries sqlQueries = new SQLQueries();
                String sqlReceta = sqlQueries.setReceta();
                try (PreparedStatement statementReceta = connection.prepareStatement(sqlReceta)) {
                    statementReceta.setInt(1, idCita);
                    statementReceta.setInt(2, receta.getIdMed());
                    statementReceta.setDate(3, receta.getFechaInicial());
                    statementReceta.setDate(4, receta.getFechaFinal());
                    statementReceta.setString(5, receta.getComentario());
                    statementReceta.setString(6, receta.getCantidadDosis());

                    statementReceta.executeUpdate();

                    connection.commit();

                }
            } catch (SQLException e) {
                // Si hay un error, revertir la transacción
                connection.rollback();
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarReceta(int idCita) {
        try (Connection connection = JDBCUtils.getConnection()) {
            String sqlEliminarDetalleConsulta = "DELETE FROM detalle_consulta WHERE id_consulta=?";
            try (PreparedStatement statementDetalleConsulta = connection.prepareStatement(sqlEliminarDetalleConsulta)) {
                statementDetalleConsulta.setInt(1, idCita);
                statementDetalleConsulta.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getObservacion(int idCita) {
        try {
            // Establecer la conexión a la base de datos
            Connection connection = JDBCUtils.getConnection();

            PreparedStatement statementMotivo = connection.prepareStatement(sqlQueries.getObservacion());
            statementMotivo.setString(1, String.valueOf(idCita));

            // Ejecutar la consulta
            ResultSet resultSetCitas = statementMotivo.executeQuery();

            // Verificar si hay resultados
            if (resultSetCitas.next()) {
                this.observacion = resultSetCitas.getString("obs_medico");
            }

            // Cerrar recursos
            resultSetCitas.close();
            statementMotivo.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return observacion;
    }

    public String getDuracion(int idCita) {
        try {
            // Establecer la conexión a la base de datos
            Connection connection = JDBCUtils.getConnection();

            PreparedStatement statementMotivo = connection.prepareStatement(sqlQueries.getDuracion());
            statementMotivo.setString(1, String.valueOf(idCita));

            // Ejecutar la consulta
            ResultSet resultSetCitas = statementMotivo.executeQuery();

            // Verificar si hay resultados
            if (resultSetCitas.next()) {
                this.duracion = resultSetCitas.getString("tipo_tratamiento");
            }

            // Cerrar recursos
            resultSetCitas.close();
            statementMotivo.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return duracion;
    }

    public void setObservacionDuracion(int idCita, String observacion, String duracion) {
        try {
            Connection connection = JDBCUtils.getConnection();

            PreparedStatement statement = connection.prepareStatement(sqlQueries.setObservacionDuracion());
            statement.setString(1, duracion);
            statement.setString(2, observacion);
            statement.setInt(3, idCita);

            statement.executeUpdate();

            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public List<Receta> obtenerReceta(int idCita) {
        List<Receta> recetas = new ArrayList<>();

        try (Connection connection = JDBCUtils.getConnection()) {

            SQLQueries sqlQueries = new SQLQueries();

            // Consulta SQL para obtener las recetas existentes para una cita específica
            String sqlRecetaExistente = sqlQueries.setRecetaExistente();

            // Preparar la declaración SQL para la consulta de recetas existentes
            PreparedStatement statementRecetaExistente = connection.prepareStatement(sqlRecetaExistente);
            statementRecetaExistente.setInt(1, idCita);

            try (ResultSet resultSet = statementRecetaExistente.executeQuery()) {
                while (resultSet.next()) {
                    // Construir un objeto Receta a partir de los datos del resultado
                    Receta receta = new Receta();
                    receta.setIdMed(resultSet.getInt("id_medicamento"));
                    receta.setFechaInicial(resultSet.getDate("fecha_inicio"));
                    receta.setFechaFinal(resultSet.getDate("fecha_fin"));
                    receta.setComentario(resultSet.getString("obs"));
                    receta.setCantidadDosis(resultSet.getString("dosis"));

                    // Agregar la receta a la lista
                    recetas.add(receta);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return recetas;
    }

}

