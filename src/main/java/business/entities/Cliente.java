package business.entities;

public abstract class Cliente {
    private int idCliente, cuota;


    /**
     * Constructor de la classe Persona.
     * @param idCliente DNI de la persona

     */
    public Cliente(int idCliente) {

        this.setIdCliente(idCliente);
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }


}