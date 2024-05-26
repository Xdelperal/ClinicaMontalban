package persistence.daos.contracts;
import persistence.exceptions.DAOException;

public interface ClienteDAO {

    String getTsiByIdcita(int idCita) throws DAOException;
}
