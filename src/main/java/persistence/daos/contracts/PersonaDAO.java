package persistence.daos.contracts;

import business.entities.Cliente;
import business.entities.Persona;
import business.entities.Personal;
import persistence.exceptions.DAOException;

public interface PersonaDAO {
    Personal getPersonalByDni(String dni) throws DAOException;
    Cliente getClienteByDni(String dni) throws DAOException;
    Persona getPersonaByDni(String dni) throws DAOException;

    void savePersonal(Personal personal) throws DAOException;
    void saveCliente(Cliente cliente) throws DAOException;
    void updatePersonal(Personal personal) throws DAOException;
    void updateCliente(Cliente cliente) throws DAOException;
    void deletePersonal(Personal personal) throws DAOException;
    void deleteCliente(Cliente cliente) throws DAOException;

}
