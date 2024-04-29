package business.entities;


import java.util.Date;

public class Receta extends Medicamento{
    //Atributos
    private int id;
    private Date fechaInicial, fechaFinal;
    private String cantidadDosis, comentario;

    // Getters and Setters

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getCantidadDosis() {
        return cantidadDosis;
    }

    public void setCantidadDosis(String cantidadDosis) {
        this.cantidadDosis = cantidadDosis;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    //Contructor
    public Receta(int id, String nombre, String dosisEstandar,Date fechaInicial, Date fechaFinal, String cantidadDosis, String comentario) {
        super(nombre, dosisEstandar);
        this.id = id;
        this.fechaInicial = fechaInicial;
        this.fechaFinal = fechaFinal;
        this.cantidadDosis = cantidadDosis;
        this.comentario = comentario;
    }

    //De momento para verificar los objetos printandolos por consola.
    @Override
    public String toString() {
        return "Receta{" +
                "id=" + id + '\'' +
                "nombre='" + getNombre() + '\'' +
                ", dosisEstandar='" + getDosisEstandar() + '\'' +
                ", fechaInicial=" + fechaInicial +
                ", fechaFinal=" + fechaFinal +
                ", cantidadDosis='" + cantidadDosis + '\'' +
                ", comentario='" + comentario + '\'' +
                '}';
    }

}
