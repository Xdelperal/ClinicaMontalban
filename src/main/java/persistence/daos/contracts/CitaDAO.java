package persistence.daos.contracts;

import business.entities.Cita;
import javafx.collections.ObservableList;

public interface CitaDAO {
    ObservableList<Cita> obtenerLista(String estadoCita, String userNameLabel);

    ObservableList<Cita> buscar(String dni);

    void crearInforme(int idCita, String obv);
}
