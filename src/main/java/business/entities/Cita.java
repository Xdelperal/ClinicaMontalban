package business.entities;

import java.sql.Date;
import java.sql.Time;

public class Cita extends Cliente {

    private int idCita;
    private String DNI, nombre, estado, descripcion, informe, obsMedico;
    private Date fecha;
    private Time hora;

    public Cita(int idCliente, int idCita, String DNI, String nombre, String estado, Date fecha, Time hora, String descripcion) {
        super(idCliente);
        this.idCita = idCita;
        this.DNI = DNI;
        this.nombre = nombre;
        this.estado = estado;
        this.fecha = fecha;
        this.hora = hora;
        this.descripcion = descripcion;
    }

    public Cita(int idCliente, int idCita, String nombre, String estado, Date fecha, Time hora, String informe, String obsMedico) {
        super(idCliente);
        this.idCita = idCita;
        this.nombre = nombre;
        this.estado = estado;
        this.fecha = fecha;
        this.hora = hora;
        this.informe = informe;
        this.obsMedico = obsMedico;
    }



    // Otros métodos, getters y setters


    public int getIdCita() { return idCita; }

    public void setIdCita(int idCita) { this.idCita = idCita; }

    public String getDNI() { return DNI; }

    public void setDNI(String DNI) { this.DNI = DNI; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }

    public String getDescripcion() { return descripcion; }

    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Date getFecha() { return fecha; }

    public void setFecha(Date fecha) { this.fecha = fecha; }

    public Time getHora() { return hora; }

    public void setHora(Time hora) { this.hora = hora; }

    public String getInforme() { return informe; }

    public void setInforme(String informe) { this.informe = informe; }

    public String getObsMedico() { return obsMedico; }

    public void setObsMedico(String obsMedico) { this.obsMedico = obsMedico; }
}
