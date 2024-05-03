package business.entities;

import java.time.LocalDate;

public class Personal extends Persona {
    private int idTrabajador, especialidad;


    /**
     * Constructor de la classe Persona.
     *
     * @param idTrabajador identificador del trabajador
     * @param especialidad especialidad del trabajador
     */

    public Personal(String dni, String nombre, String apellidos, LocalDate fechaNacimiento, int idTrabajador, int especialidad) {

        super(dni, nombre, apellidos, fechaNacimiento);

        this.setIdTrabajador(idTrabajador);
        this.setEspecialidad(especialidad);
    }
    public Personal() {
        super();


    }



    public int getIdTrabajador() {
        return idTrabajador;
    }

    public void setIdTrabajador(int idTrabajador) {
        this.idTrabajador = idTrabajador;
    }

    public int getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(int especialidad) {
        this.especialidad = especialidad;
    }



}