package persistence.daos.impl;

import business.entities.Cita;
import business.entities.Medicamento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.daos.contracts.MedicamentoDAO;
import persistence.utils.JDBCUtils;
import persistence.utils.SQLQueries;

import java.sql.*;
import java.util.Date;

public class MedicamentoJDBCDAO  implements MedicamentoDAO {
    ObservableList<Medicamento> tipos ;
    private SQLQueries sqlQueries;
    public MedicamentoJDBCDAO() {

        this.sqlQueries = new SQLQueries();
        this.tipos = FXCollections.observableArrayList(); // Inicializa citas como una nueva ObservableList
    }
    @Override
    public ObservableList<Medicamento> getTipoMedicamento() {
        try {
            // Establecer la conexión a la base de datos
            Connection connection = JDBCUtils.getConnection();

            // Consulta SQL para obtener el ID del trabajador
            String sql = "Select nombre from TiposMedicamentos";

            try {
                // Preparar la declaración SQL
                PreparedStatement statement = connection.prepareStatement(sql);

                // Ejecutar la consulta
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {


                    // Recorrer el resultado de la consulta
                    while (resultSet.next()) {
                        // Obtener los valores de las columnas
                        String nombre = resultSet.getString("nombre");

                        Medicamento medicamentoNombre = new Medicamento(nombre);
                        tipos.add(medicamentoNombre);
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

        return tipos;
    }
}
