package persistence.daos.impl;

import business.entities.Receta;
import persistence.daos.contracts.RecetaDAO;
import persistence.utils.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}

