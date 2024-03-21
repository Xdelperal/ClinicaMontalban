package business.entities;

import business.entities.Cliente;
import javafx.beans.property.*;

import java.sql.Date;
import java.sql.Time;

public class Cita extends Cliente {

    private final IntegerProperty idCita;
    private final StringProperty nombre;
    private final StringProperty estado;
    private final ObjectProperty<Date> fecha;
    private final ObjectProperty<Time> hora;
    private final StringProperty descripcion;

    public Cita(int idCita, int idCliente, String nombre, String estado, Date fecha, Time hora, String descripcion) {
        super(idCliente);
        this.idCita = new SimpleIntegerProperty(idCita);
        this.nombre = new SimpleStringProperty(nombre);
        this.estado = new SimpleStringProperty(estado);
        this.fecha = new SimpleObjectProperty<>(fecha);
        this.hora = new SimpleObjectProperty<>(hora);
        this.descripcion = new SimpleStringProperty(descripcion);
    }

    public IntegerProperty idCitaProperty() {
        return idCita;
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public StringProperty estadoProperty() {
        return estado;
    }

    public ObjectProperty<Date> fechaProperty() {
        return fecha;
    }

    public ObjectProperty<Time> horaProperty() {
        return hora;
    }

    public StringProperty descripcionProperty() {
        return descripcion;
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

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

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
}
