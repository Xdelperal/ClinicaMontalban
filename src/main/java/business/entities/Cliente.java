package business.entities;

public abstract class Cliente {
    private int idCliente, cuota;


    /**
     * Constructor de la classe Persona.
     * @param idCliente DNI de la persona
     * @param cuota Cuota pagada

     */
    public Cliente(int idCliente, int cuota) {

        this.setIdCliente(idCliente);
        this.setCuota(cuota);
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getCuota() {
        return cuota;
    }

    public void setCuota(int cuota) {
        this.cuota = cuota;
    }
}