package persistence.daos.contracts;

import business.entities.Cita;
import business.entities.Cliente;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import persistence.exceptions.DAOException;

public interface CitaDAO {
    ObservableList<Cita> obtenerLista();

}
