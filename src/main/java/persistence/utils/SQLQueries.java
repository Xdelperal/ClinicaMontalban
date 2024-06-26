package persistence.utils;

public class SQLQueries {
    /*
    String citas = "SELECT ci.idCita, c.idCliente, p.DNI, CONCAT(p.nombre, ' ', p.apellido) as nombre, ci.fecha, ci.hora, ci.descripcion, ci.estado " +
            "FROM cita ci " +
            "JOIN cliente c ON (c.idCliente = ci.idCliente) " +
            "JOIN persona p ON (p.DNI = c.DNI) " +
            "JOIN personal pe ON (ci.idTrabajador = pe.idTrabajador) " +
            "WHERE pe.DNI = ? AND ci.estado = ? AND DATE(ci.fecha) = CURDATE() " +
            "ORDER BY TIME(ci.hora) DESC";
     */

    String citas = "SELECT ci.idCita, c.idCliente, p.DNI, CONCAT(p.nombre, ' ', p.apellido) as nombre, ci.fecha, ci.hora, ci.descripcion, ci.estado " +
            "FROM cita ci " +
            "JOIN cliente c ON (c.idCliente = ci.idCliente) " +
            "JOIN persona p ON (p.DNI = c.DNI) " +
            "JOIN personal pe ON (ci.idTrabajador = pe.idTrabajador) " +
            "WHERE pe.DNI = ? AND ci.estado = ? AND ci.fecha >= CURRENT_DATE";

    String recogerCita = "SELECT ci.idCita, c.idCliente, p.DNI, CONCAT(p.nombre, \" \", p.apellido) as nombre, ci.fecha, ci.hora, ci.descripcion, ci.estado " +
            "FROM cita ci " +
            "JOIN cliente c ON (c.idCliente = ci.idCliente) " +
            "JOIN persona p ON (p.DNI = c.DNI) " +
            "JOIN personal pe ON (ci.idTrabajador = pe.idTrabajador) " +
            "WHERE pe.DNI = ? AND ci.idCita = ?";

    String cancelarCita = "UPDATE cita SET estado = 'Cancelada' WHERE idCita = ?";

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

    String crearReceta = "INSERT INTO detalle_consulta (id_consulta, id_medicamento, fecha_inicio, fecha_fin, obs, dosis) " +
            "VALUES (?,?,?,?,?,?)";

    String obtenerReceta = "SELECT * FROM detalle_consulta WHERE id_consulta = ?";

    String obtenerInforme = "SELECT informe FROM cita WHERE idCita = ?";

    String actualizarInforme = "UPDATE cita SET informe = ? WHERE idCita = ?";

    String obtenerObservacion = "SELECT obs_medico FROM consulta WHERE id_cita = ?";

    String obtenerDuracion = "SELECT tipo_tratamiento FROM consulta WHERE id_cita = ?";

    String actualizarObservacionDuracion = "UPDATE consulta SET tipo_tratamiento = ?, obs_medico = ? WHERE id_cita = ?";

    String crearConsulta ="INSERT INTO consulta (id_cita, tipo_tratamiento, codigo_barras,obs_medico) VALUES (?,?,?,?)";

    String recogerConsulta ="SELECT * FROM consulta WHERE id_cita = ?";

    String nombrePersonal = "SELECT nombre FROM persona JOIN personal WHERE persona.DNI  = ?  ";


    String recogerTsi = "SELECT c.TSI FROM cliente c JOIN cita ci ON(c.idCliente = ci.idCliente)WHERE ci.idCita= ? ";

    String actualizarEstado = "UPDATE cita SET estado = ?, informe = ? WHERE idCita = ?";

    String recogerMedico = "SELECT p.dni, p.nombre, p.apellido, p.fechaN, per.especialidad, per.idTrabajador " +
            "FROM persona p " +
            "JOIN personal per ON p.dni = per.dni " +
            "WHERE p.dni = ?";

    String recogerMedicamentos = "SELECT id, nombre, dosis_estandar, descripcion FROM Medicamentos";

    String recogerNombreMedicamento = "SELECT nombre FROM TiposMedicamentos";

    String recogerIdTipo = "SELECT id FROM TiposMedicamentos where nombre = ?";

    public String getCitas() {
        return citas;
    }

    public String getCita(){
            return recogerCita;
    }

    public String cancelCita() {return cancelarCita; }

    public String getInforme() { return obtenerInforme; }

    public String setInforme() { return actualizarInforme; }

    public String setObservacionDuracion() { return actualizarObservacionDuracion; }

    public String getObservacion() { return obtenerObservacion; }

    public String getDuracion() { return obtenerDuracion; }

    public String getMotivo() {
        return recogerMotivo;
    }

    public String setReceta() {
        return crearReceta;
    }

    public String setRecetaExistente() {
        return obtenerReceta;
    }

    public String getConsulta() {
        return recogerConsulta;
    }

    public String setConsulta() { return crearConsulta; }

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

    public String getIdTipo(){
        return recogerIdTipo;
    }

    public String getNombreMedicamento(){
        return recogerNombreMedicamento;
    }


}