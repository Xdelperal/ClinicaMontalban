package persistence.daos.impl;

import persistence.daos.contracts.ClienteDAO;
import persistence.exceptions.DAOException;
import persistence.utils.JDBCUtils;
import persistence.utils.SQLQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ClienteJDBCDAO implements ClienteDAO {
    @Override
    public String getTsiByIdcita(int idCita) throws DAOException {
        Connection connection = JDBCUtils.getConnection();

        try {
            String tsi = null; // Inicializa la variable tsi

            SQLQueries sqlQueries = new SQLQueries();
            String sqlConsulta = sqlQueries.setTsi();

            PreparedStatement statementGetConsulta = connection.prepareStatement(sqlConsulta);
            statementGetConsulta.setInt(1, idCita);

            ResultSet resultSetCitas = statementGetConsulta.executeQuery();

            if (resultSetCitas.next()) { // Verifica si hay al menos una fila en el ResultSet
                tsi = resultSetCitas.getString("TSI");
            }

            return tsi;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
        }
    }
}
