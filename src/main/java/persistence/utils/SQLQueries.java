package persistence.utils;

public class SQLQueries {

    String citas =
            "SELECT cita.idCita, cita.idCliente, persona.nombre, cita.estado, cita.fecha, cita.hora, cita.descripcion " +
                    "FROM cita " +
                    "INNER JOIN cliente ON cita.idCliente = cliente.idCliente " +
                    "INNER JOIN persona ON cliente.DNI = persona.DNI " +
                    "INNER JOIN personal ON personal.idTrabajador = cita.idTrabajador " +
                    "WHERE personal.DNI = ? AND cita.estado = ?";

    String recogerDatosPaciente =
            "SELECT cita.idCita, cita.idCliente, persona.nombre, cita.estado, cita.fecha, cita.hora, cita.descripcion " +
                    "FROM cita " +
                    "INNER JOIN cliente ON cita.idCliente = cliente.idCliente " +
                    "INNER JOIN persona ON cliente.DNI = persona.DNI " +
                    "WHERE cliente.DNI = ?";

        String crearInforme = "UPDATE consulta set obs = ? WHERE id_cita = ?  ";


    public String getCitas() {
        return citas;
    }

    public String getCrearInforme() {
        return crearInforme;
    }


    public String getRecogerDatosPaciente() {
        return recogerDatosPaciente ;
    }
}