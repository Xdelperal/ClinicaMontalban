package business.entities;

public class Medicamento {

    private String tNombre, tDscrpcion, nombre,dosisEstandar, descripcion;

    private int id, tId,gId;

    public String gettNombre() {
        return tNombre;
    }

    public void settNombre(String tNombre) {
        this.tNombre = tNombre;
    }

    public String gettDscrpcion() {
        return tDscrpcion;
    }

    public void settDscrpcion(String tDscrpcion) {
        this.tDscrpcion = tDscrpcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDosisEstandar() {
        return dosisEstandar;
    }

    public void setDosisEstandar(String dosisEstandar) {
        this.dosisEstandar = dosisEstandar;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int gettId() {
        return tId;
    }

    public void settId(int tId) {
        this.tId = tId;
    }

    public int getgId() {
        return gId;
    }

    public void setgId(int gId) {
        this.gId = gId;
    }

    public Medicamento(String tNombre, String tDscrpcion, int tId) {
        this.tNombre = tNombre;
        this.tDscrpcion = tDscrpcion;
        this.tId = tId;
    }

    //Constructor con el nombre del tipo de Medicamento
    public Medicamento(String Medicamento){
        this.tNombre=Medicamento;
    }

    public Medicamento(String nombre, String dosisEstandar, String descripcion, int id, int gId) {
        this.nombre = nombre;
        this.dosisEstandar = dosisEstandar;
        this.descripcion = descripcion;
        this.id = id;
        this.gId = gId;
    }

    public Medicamento(int id, String nombre, String dosisEstandar, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.dosisEstandar = dosisEstandar;
        this.descripcion = descripcion;
    }


    public Medicamento(String nombre, String dosisEstandar, String descripcion) {
        this.nombre = nombre;
        this.dosisEstandar = dosisEstandar;
        this.descripcion = descripcion;
    }

}
