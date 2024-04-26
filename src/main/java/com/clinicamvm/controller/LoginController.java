package com.clinicamvm.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import persistence.utils.JDBCUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

   /**
    * Declaracion de constantes privadas
    */

    @FXML
    private TextField fieldDNI;

    @FXML
    private PasswordField fieldContrasena;

    @FXML
    private Label msgLabel;

    // Declarar la variable stage
    private Stage stage;


/**
* Este es el metodo  que las credenciales coinciden en la base de datos.
* y que el inicioha sido exitoso, tambien comprueba que los datos han sido rellenados
* de forma correta.
*/
    @FXML
    public void comprobacion(ActionEvent e) throws IOException {
        String dni = fieldDNI.getText();
        String contrasena = fieldContrasena.getText();

        if (dni.isBlank() || contrasena.isBlank()) {
            msgLabel.setText("No has introducido los datos!");
            msgLabel.setWrapText(true);
        } else {
            // Hashear la contraseña
            String hashedContrasena = hashPassword(contrasena);

            // Realizar la autenticación
            String result = autenticarUsuario(dni, hashedContrasena);

            switch (result) {
                case "success":
                    // Usuario autenticado, redirigir a la página principal
                    cargarMainPanel();
                    break;
                case "Contraseña incorrecta":
                case "Usuario no encontrado":
                case "Error de base de datos":
                    // Mostrar mensaje de error en el panel
                    msgLabel.setText(result);
                    break;
            }
        }
    }


/**
* Este metodo es el que redirecciona a la web cunando pulsas el link
* de recuperacion de credenciales en caso de perdida.
*/
    @FXML
    private void abrirSoporte(ActionEvent event) {
        try {
            URI uri = new URI("https://google.com");
            java.awt.Desktop.getDesktop().browse(uri);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }



/**
 * Este metodo es el encargado de instanciar el inicializador del FXML que es la parte visual,
 * despues creamos el Pane que es el panel que mostraremos al cargarlo y despues le indicamos el
 * controlador encargado de la logica de ese panel
 *
 *
 */
@FXML
private void cargarMainPanel() {
    try {
        // Cargar la vista del panel principal y obtener su controlador
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ui/main.fxml"));
        ScrollPane root = loader.load();
        MainPanelController mainPanelController = loader.getController();

        // Actualizar el texto del Label en el MainPanelController
        mainPanelController.updateUserNameLabel(fieldDNI.getText());

        // Obtener el Stage actual y configurar la nueva escena
        Stage stage = (Stage) fieldDNI.getScene().getWindow();
        Scene scene = new Scene(root);

        // Establecer la escena en la ventana
        stage.setScene(scene);
        stage.setResizable(true); // Permitir redimensionar la ventana

        // Obtener el tamaño de la pantalla
        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        double screenWidth = primaryScreenBounds.getWidth();
        double screenHeight = primaryScreenBounds.getHeight();

        // Establecer el tamaño de la ventana para que ocupe toda la pantalla
        stage.setWidth(screenWidth);
        stage.setHeight(screenHeight);

        // Centrar la ventana en la pantalla
        stage.centerOnScreen();

        Image icono = new Image(getClass().getResourceAsStream("/com/ui/img/logo.png"), 200, 200, true, true);
        stage.getIcons().add(icono);

    } catch (IOException e) {
        e.printStackTrace();
    }
}



    /**
 * Este metodo es el encargado de hashear las contraseñas introducido
 * en el campo de contraseña para que despues coincida con del de la base de datos
 */
    private String hashPassword(String contrasena) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(contrasena.getBytes());

            //Convertir el hash en una cadena hexadecimal
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

    private static Connection dbLink;



/**
 * Este metodo es el que ejecuta la query para la comprobacion de las
 * credenciales del login coincidan con las almacenadas en la de la base de datos.
 */
private String autenticarUsuario(String dni, String hashedPassword) {
    JDBCUtils jdbcUtils = new JDBCUtils();
    try (Connection connection = jdbcUtils.getConnection()) {
        String query = "SELECT password FROM personal WHERE DNI=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, dni);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String storedPassword = resultSet.getString("password");
                    if (hashedPassword.equals(storedPassword)) {
                        // Autenticación exitosa
                        return "success";
                    } else {
                        // Contraseña incorrecta
                        return "Contraseña incorrecta";
                    }
                } else {
                    // Usuario no encontrado
                    return "Usuario no encontrado";
                }
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Error de base de datos
        return "Error de base de datos";
    }
}


    public void setStage(Stage stage) {
        this.stage = stage;
        // Configurar la ventana para que no sea redimensionable
        stage.setResizable(false);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {



    }
}
