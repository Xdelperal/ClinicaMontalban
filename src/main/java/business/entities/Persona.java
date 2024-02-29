package business.entities;

import java.time.LocalDate;
import java.time.Period;

public abstract class Persona {
    private String dni, nombre, apellidos;
    private LocalDate fechaNacimiento;

    //<editor-fold defaultstate="collapsed" desc="Constructor">

    /**
     * Constructor de la classe Persona.
     * @param dni DNI de la persona
     * @param nom Nom de la persona
     * @param apellidos Cognoms de la persona
     * @param fechaNacimiento Data de naixement de la persona
     */
    public Persona(String dni, String nom, String apellidos, LocalDate fechaNacimiento, String genere) {
        this.setDni(dni);
        this.setNombre(nom);
        this.setCognoms(apellidos);
        this.setFechaNacimiento(this.fechaNacimiento);
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

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Setters">

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



    //</editor-fold>

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
     * @param integer Enter apellidos convertir
     * @return Cadena de text que representa l'enter
     */
    public String toString(int integer){
        StringBuilder sb = new StringBuilder();
        sb.append(integer);
        return sb.toString();
    }

    //</editor-fold>

}