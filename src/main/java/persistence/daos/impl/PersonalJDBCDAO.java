package persistence.daos.impl;

import business.entities.Personal;
import persistence.daos.contracts.PersonalDAO;
import persistence.exceptions.DAOException;
import persistence.utils.SQLQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonalJDBCDAO implements PersonalDAO {
    private Connection connection;

    public PersonalJDBCDAO(Connection connection) {
        this.connection = connection;
    }

    public PersonalJDBCDAO() {

    }

    @Override
    public Personal getPersonalByDni(String dni) throws DAOException {
        try {

            SQLQueries sqlQueries = new SQLQueries();
            String sql = sqlQueries.getMedico();

            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, dni);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Crear una instancia de Personal con los datos recuperados de la consulta
                int especialidad = resultSet.getInt("especialidad");
                int idTrabajador = resultSet.getInt("idTrabajador");
                return new Personal(
                        resultSet.getString("dni"),
                        resultSet.getString("nombre"),
                        resultSet.getString("apellido"),
                        resultSet.getDate("fechaN").toLocalDate(),
                        idTrabajador,
                        especialidad
                );
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DAOException("Error al obtener personal por DNI", e);
        }
    }


    // Implementa los métodos savePersonal, updatePersonal y deletePersonal de manera similar
    // Utilizando consultas SQL adecuadas para insertar, actualizar y eliminar registros en la base de datos
}
