package persistence.daos.impl;

import persistence.daos.contracts.PersonalDAO;

public class PersonalJDBCDAO implements PersonalDAO {


        private static String usuario;
        private static String contrasena;

            public static void guardarCredenciales(String usuario, String contrasena) {
            Personal.usuario = usuario;
            Personal.contrasena = contrasena;
        }

        public static String getUsuario() {
            return usuario;
        }

        public static String getContrasena() {
            return contrasena;
        }

        public static Medico getMedicoByDni(String dni) {
            // Aquí puedes realizar la lógica para obtener un objeto Medico basado en el DNI proporcionado.
            // Esto puede variar dependiendo de cómo esté estructurada tu aplicación.
            // Por ejemplo, podrías tener una clase DAO para obtener los datos del médico desde la base de datos.
            // Por ahora, vamos a simular la obtención de un médico con un DNI específico.

            if ("12345678A".equals(dni)) { // Supongamos que este es el DNI del médico
                Medico medico = new Medico();
                medico.guardarCredenciales("usuarioMedico", "contrasenaMedico"); // Guardamos las credenciales
                return medico;
            } else {
                return null; // Devolvemos null si no se encuentra un médico con el DNI proporcionado
            }
        }
    }




}
