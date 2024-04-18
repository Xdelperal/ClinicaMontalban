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

    @Override
    public ObservableList<Medicamento> getMedicamentos(String grupoMedicamento) {
        System.out.println("entra en el getMedicamentos");
        try {
            Connection connection = JDBCUtils.getConnection();

            String sql = "SELECT id FROM TiposMedicamentos where nombre=?";


            try {
                // Preparar la declaración SQL
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1,grupoMedicamento);

                // Ejecutar la consulta
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {

                    int idGrupo = resultSet.getInt("id");
                    System.out.println("El id del tipo de medicamento en cuestion es: "+idGrupo);
                    // Consulta SQL para obtener las citas pendientes con el nombre del cliente
                    String sqlCitas ="SELECT nombre, dosis_estandar, descripcion FROM Medicamentos WHERE grupoid=? ";

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
                        System.out.println("la info frl  medicamento a añadir es: "+nombre + " "+ dosis_estandar + " " + descripcion);

                        // Crear un objeto Cita con los datos obtenidos
                        Medicamento nuevoMedicamento = new Medicamento(nombre, dosis_estandar, descripcion);
                        Medicamentos.add(nuevoMedicamento);
                    }
                }

                System.out.println("la lista mide:"+Medicamentos.size());
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
