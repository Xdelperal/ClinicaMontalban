package business.entities;


import java.util.Date;

public class Receta extends Medicamento{
    //Atributos
    private int id, idMed;
    private Date fechaInicial, fechaFinal;
    private String cantidadDosis, comentario;


    // Getters and Setters

    public int getIdMed() {
        return idMed;
    }

    public void setIdMed(int idMed) {
        this.idMed = idMed;
    }

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

    public java.sql.Date getFechaFinal() {
        return (java.sql.Date) fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public java.sql.Date getFechaInicial() {
        return (java.sql.Date) fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    //Contructor
    public Receta(int id, int idMed,String nombre, String dosisEstandar,Date fechaInicial, Date fechaFinal, String cantidadDosis, String comentario) {
        super(nombre, dosisEstandar);
        this.id = id;
        this.idMed = idMed;
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
                "idMed=" + idMed + '\'' +
                "nombre='" + getNombre() + '\'' +
                ", dosisEstandar='" + getDosisEstandar() + '\'' +
                ", fechaInicial=" + fechaInicial +
                ", fechaFinal=" + fechaFinal +
                ", cantidadDosis='" + cantidadDosis + '\'' +
                ", comentario='" + comentario + '\'' +
                '}';
    }

}
