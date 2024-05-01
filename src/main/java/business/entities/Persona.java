package business.entities;

import persistence.utils.JDBCUtils;
import persistence.utils.SQLQueries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

public class Persona {
    private String dni, nombre, apellidos;
    private LocalDate fechaNacimiento;

    //<editor-fold defaultstate="collapsed" desc="Constructor">

    /**
     * Constructor de la classe Persona.
     *
     * @param dni             DNI de la persona
     * @param nom             Nom de la persona
     * @param apellidos       Cognoms de la persona
     * @param fechaNacimiento Data de naixement de la persona
     */
    public Persona(String dni, String nom, String apellidos, LocalDate fechaNacimiento) {
        this.setDni(dni);
        this.setNombre(nom);
        this.setCognoms(apellidos);
        this.setFechaNacimiento(fechaNacimiento);
    }

    public Persona(String dni) {
        this.setDni(dni);
    }


    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters">

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCognoms() {
        return apellidos;
    }

    public LocalDate getDataNaixement() {
        return fechaNacimiento;
    }

    private SQLQueries sqlQueries;

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCognoms(String cognoms) {
        this.apellidos = cognoms;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }




    //<editor-fold defaultstate="collapsed" desc="API pública">
    /**
     * Calcula l'edat apellidos partir d'una data de naixement.
     * @param birthday Data de naixement
     * @return Edat actual calculada en anys
     */
    public int getAge(LocalDate birthday) {
        return Period.between(birthday, LocalDate.now()).getYears();
    }

    /**
     * Converteix un enter en una cadena de text.
     * @param dni Enter apellidos convertir
     * @return Cadena de text que representa l'enter
     */

    public String getPersona(String dni) {
        try {
            // Establecer la conexión a la base de datos
            Connection connection = JDBCUtils.getConnection();
            SQLQueries sqlQueries1 = new SQLQueries();
            PreparedStatement statementMotivo = connection.prepareStatement(sqlQueries1.getPersonal());
            statementMotivo.setString(1, String.valueOf(dni));

            // Ejecutar la consulta
            ResultSet resultSetCitas = statementMotivo.executeQuery();

            // Verificar si hay resultados
            if (resultSetCitas.next()) {
                this.nombre = resultSetCitas.getString("nombre");
            }

            // Cerrar recursos
            resultSetCitas.close();
            statementMotivo.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // Manejar la excepción o lanzarla nuevamente si es necesario
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return nombre;
    }

}