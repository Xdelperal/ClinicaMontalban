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
}
