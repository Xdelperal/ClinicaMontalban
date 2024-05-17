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

        public void insertarReceta(Receta receta, int idCita) {
            try (Connection connection = JDBCUtils.getConnection()) {
                // Iniciar una transacción
                connection.setAutoCommit(false);

                try {
                    // Eliminar recetas existentes para la cita
                    eliminarReceta(connection, idCita);
                    SQLQueries sqlQueries = new SQLQueries();
                    // Insertar la nueva receta

                    String sqlReceta = sqlQueries.setReceta();
                    try (PreparedStatement statementReceta = connection.prepareStatement(sqlReceta)) {
                        statementReceta.setInt(1, idCita);
                        statementReceta.setInt(2, receta.getIdMed());
                        statementReceta.setDate(3, receta.getFechaInicial());
                        statementReceta.setDate(4, receta.getFechaFinal());
                        statementReceta.setString(5, receta.getComentario());
                        statementReceta.setString(6, receta.getCantidadDosis());

                        int rowsAffected = statementReceta.executeUpdate();

                        // Confirmar la transacción si todo es exitoso
                        connection.commit();

                        if (rowsAffected > 0) {
                            System.out.println("Receta insertada correctamente.");
                        } else {
                            System.out.println("No se pudo insertar la receta.");
                        }
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

        private void eliminarReceta(Connection connection, int idCita) throws SQLException {
            String sqlEliminarDetalleConsulta = "DELETE FROM detalle_consulta WHERE id_consulta=?";
            try (PreparedStatement statementDetalleConsulta = connection.prepareStatement(sqlEliminarDetalleConsulta)) {
                statementDetalleConsulta.setInt(1, idCita);
                statementDetalleConsulta.execute();
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

