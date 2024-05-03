package persistence.daos.contracts;
import business.entities.Personal;
import persistence.exceptions.DAOException;

public interface PersonalDAO {
    Personal getPersonalByDni(String dni) throws DAOException;

}
