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
        try {
            // Establecer la conexión a la base de datos
            Connection connection = JDBCUtils.getConnection();
            {

                SQLQueries sqlQueries = new SQLQueries();


                // Consulta SQL para obtener las citas pendientes con el nombre del cliente
                String sqlReceta = sqlQueries.setReceta();

                // Preparar la declaración SQL para la segunda consulta
                PreparedStatement statementReceta = connection.prepareStatement(sqlReceta);

                statementReceta.setInt(1, idCita);
                statementReceta.setInt(2, receta.getIdMed());
                statementReceta.setDate(3, receta.getFechaInicial());
                statementReceta.setDate(4, receta.getFechaFinal());
                statementReceta.setString(5, receta.getComentario());
                statementReceta.setString(6, receta.getCantidadDosis());

                boolean insertExitoso = statementReceta.execute();

                if (insertExitoso) {
                    System.out.println("Receta insertada correctamente.");
                } else {
                    System.out.println("No se pudo insertar la receta.");
                }

                // Recorrer el resultado de la consulta
            }

            // Cerrar recursos
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Receta> obtenerReceta(int idCita) {
        List<Receta> recetas = new ArrayList<>();

        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM detalle_consulta WHERE id_consulta = ?")) {

            statement.setInt(1, idCita);

            try (ResultSet resultSet = statement.executeQuery()) {
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

    public void actualizarReceta(Receta receta, int idCita) {
        try (Connection connection = JDBCUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE detalle_consulta SET fecha_inicio = ?, fecha_fin = ?, dosis = ?, obs = ? WHERE id_medicamento = ? AND id_consulta = ?")) {

            statement.setDate(1, receta.getFechaInicial());
            statement.setDate(2, receta.getFechaFinal());
            statement.setString(3, receta.getCantidadDosis());
            statement.setString(4, receta.getComentario());
            statement.setInt(5, receta.getIdMed());
            statement.setInt(6, idCita);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Receta actualizada correctamente.");
            } else {
                System.out.println("No se pudo actualizar la receta.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

