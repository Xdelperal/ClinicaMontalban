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

    String recogerMotivo=
            "SELECT cita.descripcion " +
                    "FROM cita " +
                    "INNER JOIN cliente ON cita.idCliente = cliente.idCliente " +
                    "INNER JOIN persona ON cliente.DNI = persona.DNI " +
                    "WHERE cita.idCita = ?";

    String crearInforme = "UPDATE consulta set obs = ? WHERE id_cita = ?  ";

    String crearReceta = "INSERT INTO detalle_consulta (id_consulta, id_medicamento, fecha_inicio, fecha_fin, obs, dosis) " +
            "VALUES (?,?,?,?,?,?)";

    String crearConsulta ="INSERT INTO consulta (id_cita, tipo_tratamiento) VALUES (?,?)";

    String nombrePersonal = "SELECT nombre FROM persona JOIN personal WHERE persona.DNI  = ?  ";

    public String getCitas() {
        return citas;
    }


    public String getCrearInforme() {
        return crearInforme;
    }

    public String getMotivo() {
        return recogerMotivo;
    }

    public String getPersonal() {
        return nombrePersonal;
    }

    public String setReceta() {
        return crearReceta;
    }

    public String setConsulta() {
        return crearConsulta;
    }

    public String getRecogerDatosPaciente() {
        return recogerDatosPaciente ;
    }
}