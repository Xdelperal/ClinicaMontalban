package com.clinicamvm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import persistence.utils.JDBCUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Controller {

    @FXML
    private TextField fieldDNI;

    @FXML
    private PasswordField fieldContrasena;

    @FXML
    private Label msgLabel;

    @FXML
    public void comprobacion(ActionEvent e) {
        String dni = fieldDNI.getText();
        String contrasena = fieldContrasena.getText();

        if (dni.isBlank() || contrasena.isBlank()) {
            msgLabel.setText("No has introducido datos!");
        } else {

            // Hashear la contraseña
            String hashedContrasena = hashPassword(contrasena);

            // Realizar la autenticación
            boolean autenticado = autenticarUsuario(dni, hashedContrasena);

            if (autenticado) {
                // Usuario autenticado, redirigir a la página principal
                msgLabel.setText("Inicio de sesión exitoso!");
                // Aquí puedes agregar el código para redirigir a la página principal
            } else {
                // Usuario no autenticado, mostrar mensaje de error
                msgLabel.setText("Error en el inicio de sesión. Por favor, verifica tus credenciales.");
            }
        }
    }

    // Método para hashear la contraseña usando el algoritmo SHA-512
    private String hashPassword(String contrasena) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(contrasena.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Método para autenticar al usuario en la base de datos
    private boolean autenticarUsuario(String dni, String password) {
        JDBCUtils jdbcUtils = new JDBCUtils();
        try (Connection connection = jdbcUtils.getConnection()) {
            String query = "SELECT DNI, password FROM personal WHERE DNI=? and password=?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, dni);
                statement.setString(2, password);
                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
