package persistence.daos.impl;

import business.entities.Medicamento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.daos.contracts.MedicamentoDAO;
import persistence.utils.JDBCUtils;
import persistence.utils.SQLQueries;

import java.sql.*;

public class MedicamentoJDBCDAO  implements MedicamentoDAO {

    ObservableList<Medicamento> tipos ;
    ObservableList<Medicamento> Medicamentos ;


    private SQLQueries sqlQueries;
    public MedicamentoJDBCDAO() {

        this.sqlQueries = new SQLQueries();
        this.tipos = FXCollections.observableArrayList(); // Inicializa citas como una nueva ObservableList
        this.Medicamentos = FXCollections.observableArrayList();

    }
    @Override
    public ObservableList<Medicamento> getTipoMedicamento() {

        try {
            String sql;

            // Establecer la conexión a la base de datos
            Connection connection = JDBCUtils.getConnection();

            // Consulta SQL para obtener el ID del trabajador
            sql = sqlQueries.getNombreMedicamento();

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

    @Override
    public ObservableList<Medicamento> getMedicamentos(String grupoMedicamento) {

        try {
            String sql;
            Connection connection = JDBCUtils.getConnection();
                 sql = sqlQueries.getIdTipo();


            try {
                // Preparar la declaración SQL
                PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, grupoMedicamento);

                // Ejecutar la consulta
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {

                    int idGrupo = resultSet.getInt("id");
                    String sqlCitas;
                    // Consulta SQL para obtener las citas pendientes con el nombre del cliente

                         sqlCitas = "SELECT nombre, dosis_estandar, descripcion FROM Medicamentos WHERE grupoid = ? ";


                    // Preparar la declaración SQL para la segunda consulta
                    PreparedStatement statementCitas = connection.prepareStatement(sqlCitas);
                    statementCitas.setInt(1, idGrupo);

                    // Ejecutar la segunda consulta
                    ResultSet resultSet1 = statementCitas.executeQuery();

                    // Recorrer el resultado de la consulta
                    while (resultSet1.next()) {
                        // Obtener los valores de las columnas

                        String nombre = resultSet1.getString("nombre");
                        String dosis_estandar = resultSet1.getString("dosis_estandar");

                        String descripcion = resultSet1.getString("descripcion");

                        // Crear un objeto Cita con los datos obtenidos
                        Medicamento nuevoMedicamento = new Medicamento(nombre, dosis_estandar, descripcion);
                        Medicamentos.add(nuevoMedicamento);
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


        return Medicamentos;
    }

    @Override
    public ObservableList<Medicamento> getMedicamentos() {
        try {
            String sql;
            Connection connection = JDBCUtils.getConnection();

            // Consulta SQL para obtener todos los medicamentos
            sql = sqlQueries.getMedicamentos();

            try {
                // Preparar la declaración SQL
                PreparedStatement statement = connection.prepareStatement(sql);

                // Ejecutar la consulta
                ResultSet resultSet = statement.executeQuery();

                // Recorrer el resultado de la consulta
                while (resultSet.next()) {
                    // Obtener los valores de las columnas
                    int id = resultSet.getInt("id");
                    String nombre = resultSet.getString("nombre");
                    String dosis_estandar = resultSet.getString("dosis_estandar");
                    String descripcion = resultSet.getString("descripcion");

                    // Crear un objeto Medicamento con los datos obtenidos
                    Medicamento nuevoMedicamento = new Medicamento(id, nombre, dosis_estandar, descripcion);
                    Medicamentos.add(nuevoMedicamento);
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

        return Medicamentos;
    }


}
