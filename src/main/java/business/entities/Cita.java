package business.entities;

import business.entities.Cliente;
import javafx.beans.property.*;

import java.awt.*;
import java.sql.Date;
import java.sql.Time;

public class Cita extends Cliente {

    private final IntegerProperty idCita;
    private final StringProperty nombre;
    private final StringProperty DNI, estado, descripcion;
    private final ObjectProperty<Date> fecha;
    private final ObjectProperty<Time> hora;


    public Cita(StringProperty nombre, StringProperty DNI, StringProperty estado, StringProperty descripcion, ObjectProperty<Date> fecha, ObjectProperty<Time> hora) {
        this.nombre = nombre;
        this.DNI = DNI;
        this.estado = estado;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
    }

    public Cita(int idCita, int idCliente, String nombre, String estado, Date fecha, Time hora, String descripcion) {
        super(idCliente);
        this.idCita = new SimpleIntegerProperty(idCita);
        this.nombre = new SimpleStringProperty(nombre);
        this.estado = new SimpleStringProperty(estado);
        this.fecha = new SimpleObjectProperty<>(fecha);
        this.hora = new SimpleObjectProperty<>(hora);
        this.descripcion = new SimpleStringProperty(descripcion);
    }




    // Otros métodos, getters y setters

    public int getIdCita() {
        return idCita.get();
    }

    public void setIdCita(int idCita) {
        this.idCita.set(idCita);
    }

    public String getNombre() {
        return nombre.get();
    }

    public void setNombre(String nombre){ this.nombre.set(nombre); }

    public String getEstado() {
        return estado.get();
    }

    public void setEstado(String estado) {
        this.estado.set(estado);
    }

    public Date getFecha() {
        return fecha.get();
    }

    public void setFecha(Date fecha) {
        this.fecha.set(fecha);
    }

    public Time getHora() {
        return hora.get();
    }

    public void setHora(Time hora) {
        this.hora.set(hora);
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public String getDNI() { return DNI.get(); }

    public void setDNI(String DNI) { this.DNI.set(DNI); }
}
