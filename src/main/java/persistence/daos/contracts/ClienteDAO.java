package persistence.daos.contracts;
import persistence.exceptions.DAOException;
import business.entities.Cliente;

public interface ClienteDAO {
    Cliente getClienteByDni(String dni) throws DAOException;
}
