package business.entities;

import java.sql.Date;
import java.sql.Time;

public abstract class Cita extends Cliente {

    private int idCita;
    private Date fecha;
    private Time hora;
    private String descripcion;
    public Cita(int idCita, int idCliente, int cuota, Date fecha, Time hora, String descripcion) {
        super(idCliente, cuota);

    this.setIdCita(idCita);
    this.setFecha(fecha);
    this.setHora(hora);
    this.setDescripcion(descripcion);

    }

    public int getIdCita() {
        return idCita;
    }

    public void setIdCita(int idCita) {
        this.idCita = idCita;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Time getHora() {
        return hora;
    }

    public void setHora(Time hora) {
        this.hora = hora;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}