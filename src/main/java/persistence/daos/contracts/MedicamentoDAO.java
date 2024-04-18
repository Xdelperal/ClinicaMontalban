package persistence.daos.contracts;

import business.entities.Medicamento;
import javafx.collections.ObservableList;

public interface MedicamentoDAO {

    ObservableList<Medicamento> getTipoMedicamento();
    ObservableList<Medicamento> getMedicamentos(String grupoMedicamento);

    ObservableList<Medicamento> getMedicamentos();


}
