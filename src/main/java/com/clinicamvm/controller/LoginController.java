package com.clinicamvm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import persistence.utils.JDBCUtils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField fieldDNI;

    @FXML
    private PasswordField fieldContrasena;

    @FXML
    private Label msgLabel;
    @FXML
    private Stage stage;
    private MainPanelController mainPanelController; // Referencia al controlador del MainPanel


    public void setMainPanelController(MainPanelController mainPanelController) {
        this.mainPanelController = mainPanelController;
    }


    @FXML
    public void comprobacion(ActionEvent e) throws IOException {
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

                // Obtener el Stage del login
                Stage loginStage = (Stage) fieldDNI.getScene().getWindow();

                // Cargar la vista del panel principal
                FXMLLoader mainPanelLoader = new FXMLLoader(getClass().getResource("/com/ui/mainPanel.fxml"));
                Parent mainPanelRoot = mainPanelLoader.load();
                MainPanelController mainPanelController = mainPanelLoader.getController();

                // Verificar si mainPanelController es null antes de configurarlo


                // Configurar la escena y mostrarla en el escenario
                Scene scene = new Scene(mainPanelRoot);
                loginStage.setScene(scene);
                loginStage.show();
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

            // Convertir el hash en una cadena hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = String.format("%02x", b);
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }



    // Método para autenticar al usuario en la base de datos
    private boolean autenticarUsuario(String dni, String hashedPassword) {
        JDBCUtils jdbcUtils = new JDBCUtils();
        try (Connection connection = jdbcUtils.getConnection()) {
            String query = "SELECT password FROM personal WHERE DNI=?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, dni);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String storedPassword = resultSet.getString("password");
                        return hashedPassword.equals(storedPassword);
                    } else {
                        // El usuario no existe en la base de datos
                        return false;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
