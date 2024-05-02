package persistence.daos.contracts;
import persistence.exceptions.DAOException;
import business.entities.Cliente;

public interface ClienteDAO {

    String getTsiByIdcita(int idCita) throws DAOException;
}
