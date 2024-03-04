package persistence.daos.impl;

import business.entities.Persona;
import persistence.daos.contracts.PersonaDAO;
import persistence.exceptions.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class PersonaJDBCDAO implements PersonaDAO {
    private Connection connection;

    public PersonaJDBCDAO(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Persona getPersonaByDni(String dni) throws DAOException {
        try {
            String sql = "SELECT * FROM personas WHERE dni = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, dni);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                // Construye y devuelve una instancia de Persona basada en los datos del resultado
                // Aquí deberías manejar la lógica de crear la instancia de Persona y sus subclases si es necesario
                return new Persona(resultSet.getString("dni"), resultSet.getString("nombre"), resultSet.getString("apellidos"), resultSet.getDate("fecha_nacimiento").toLocalDate());
            } else {
                return null; // No se encontró ninguna persona con el DNI dado
            }
        } catch (SQLException e) {
            throw new DAOException("Error al obtener persona por DNI", e);
        }
    }

    // Implementa los métodos savePersona, updatePersona y deletePersona de manera similar
    // Utilizando consultas SQL adecuadas para insertar, actualizar y eliminar registros en la base de datos
}
