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

    String crearReceta = "INSERT IGNORE INTO detalle_consulta (id_consulta, id_medicamento, fecha_inicio, fecha_fin, obs, dosis) " +
            "VALUES (?,?,?,?,?,?)";

    String crearConsulta ="INSERT IGNORE INTO consulta (id_cita, tipo_tratamiento, codigo_barras) VALUES (?,?,?)";

    String recogerConsulta ="SELECT * FROM consulta WHERE id_cita = ?";


    String nombrePersonal = "SELECT nombre FROM persona JOIN personal WHERE persona.DNI  = ?  ";


    String recogerTsi = "SELECT c.TSI FROM cliente c JOIN cita ci ON(c.idCliente = ci.idCliente)WHERE ci.idCita= ? ";

    String actualizarEstado = "UPDATE cita SET estado = ?, informe = ? WHERE idCita = ?";

    String recogerMedico = "SELECT p.dni, p.nombre, p.apellido, p.fechaN, per.especialidad, per.idTrabajador " +
            "FROM persona p " +
            "JOIN personal per ON p.dni = per.dni " +
            "WHERE p.dni = ?";

    String recogerMedicamentos = "SELECT id, nombre, dosis_estandar, descripcion FROM Medicamentos";

    String recogerMedicamento = "SELECT * FROM Medicamentos WHERE nombre = ?";


    String recogerNombreMedicamento = "SELECT nombre FROM TiposMedicamentos";

    String recogerIdTipo = "SELECT id FROM TiposMedicamentos where nombre = ?";

    public String getCitas() {
        return citas;
    }

    public String getMotivo() {
        return recogerMotivo;
    }

    public String setReceta() {
        return crearReceta;
    }

    public String getConsulta() {
        return recogerConsulta;
    }

    public String setConsulta() {
        return crearConsulta;
    }

    public String setTsi(){
        return recogerTsi;
    }

    public String getRecogerDatosPaciente() {
        return recogerDatosPaciente ;
    }

    public String getMedico(){
        return recogerMedico;
    }

    public String updateEstado(){
        return actualizarEstado;
    }

    public String getMedicamentos(){
        return recogerMedicamentos;
    }
    public String getMedicamento(){
        return recogerMedicamento;
    }

    public String getIdTipo(){
        return recogerIdTipo;
    }


    public String getNombreMedicamento(){
        return recogerNombreMedicamento;
    }


}