package com.clinicamvm.controller;

import business.entities.Personal;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import persistence.daos.impl.PersonalJDBCDAO;
import persistence.exceptions.DAOException;
import persistence.utils.JDBCUtils;

import javafx.scene.image.ImageView;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class LoginController implements Initializable {

   /**
    * Declaracion de constantes privadas
    */

    @FXML
    private JFXTextField fieldDNI;
    @FXML
    private JFXPasswordField fieldContrasena;
    @FXML
    private JFXTextField fieldContrasenaVisible;
    @FXML
    private Label msgLabel;
    private Stage stage;

    @FXML
    private ImageView eyeIcon;

    private boolean passwordVisible = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        msgLabel.setVisible(false);
        // Expresión regular para aceptar máximo 8 dígitos seguidos de una letra opcional
        Pattern pattern = Pattern.compile("\\d{0,8}[a-zA-Z]?");

        // Aplicar el TextFormatter al TextField de DNI
        fieldDNI.setTextFormatter(new TextFormatter<>(new UnaryOperator<TextFormatter.Change>() {
            @Override
            public TextFormatter.Change apply(TextFormatter.Change change) {
                // Validar la nueva entrada con la expresión regular
                if (pattern.matcher(change.getControlNewText()).matches()) {
                    return change;
                } else {
                    // Si la entrada no coincide con la expresión regular, se rechaza
                    return null;
                }
            }
        }));

        togglePasswordVisibility();
    }


/**
* Este es el metodo  que las credenciales coinciden en la base de datos.
* y que el inicioha sido exitoso, tambien comprueba que los datos han sido rellenados
* de forma correta.
*/
@FXML
public void comprobacion(ActionEvent e) throws IOException {
    togglePasswordVisibility();
    String dni = fieldDNI.getText();
    String contrasena = fieldContrasena.getText();

    if (dni.isBlank() || contrasena.isBlank()) {
        msgLabel.setText("No has introducido los datos!");
        msgLabel.setWrapText(true);
        msgLabel.setVisible(true);
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
                // Hacer visible el msgLabel si falla la autenticación
                msgLabel.setVisible(true);
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
        String osName = System.getProperty("os.name").toLowerCase();

        // Verificar el sistema operativo y abrir el cliente de correo electrónico correspondiente
        if (osName.contains("win")) {
            // Para Windows
            String[] command = {"cmd", "/c", "start", "mailto:soporte@clinicamontalban.com"};
            Process process = Runtime.getRuntime().exec(command);
        } else if (osName.contains("mac")) {
            // Para MacOS
            String[] command = {"open", "mailto:soporte@clinicamontalban.com"};
            Process process = Runtime.getRuntime().exec(command);
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            // Para Linux/Unix
            String[] command = {"xdg-open", "mailto:soporte@clinicamontalban.com"};
            Process process = Runtime.getRuntime().exec(command);
        } else {
            // Sistema operativo desconocido, manejar según sea necesario
            System.out.println("Sistema operativo no compatible.");
        }
    } catch (IOException e) {
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
        Connection connection = JDBCUtils.getConnection();
        // Cargar la vista del panel principal y obtener su controlador
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/ui/main.fxml"));
        ScrollPane root = loader.load();
        MainPanelController mainPanelController = loader.getController();

        Personal medico = new Personal();
        PersonalJDBCDAO personalJDBCDAO = new PersonalJDBCDAO(connection);
        medico = personalJDBCDAO.getPersonalByDni(fieldDNI.getText());

        // Actualizar el texto del Label en el MainPanelController
        mainPanelController.setMedico(medico);
        mainPanelController.updateUserNameLabel(medico);
        mainPanelController.getPendiente();

        // Obtener el Stage actual y configurar la nueva escena
        Stage stage = (Stage) fieldDNI.getScene().getWindow();
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

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
    } catch (DAOException e) {
        throw new RuntimeException(e);
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

    @FXML
    private void togglePasswordVisibility() {
        // Almacena el contenido actual de ambos campos de contraseña
        String passwordText = fieldContrasena.getText();
        String visiblePasswordText = fieldContrasenaVisible.getText();

        // Guarda el campo de contraseña que debe quedar seleccionado
        TextField selectedField = passwordVisible ? fieldContrasena : fieldContrasenaVisible;

        // Cambia la visibilidad de ambos campos de contraseña según su estado actual
        fieldContrasena.setVisible(!passwordVisible);
        fieldContrasena.setManaged(!passwordVisible);
        fieldContrasenaVisible.setVisible(passwordVisible);
        fieldContrasenaVisible.setManaged(passwordVisible);

        // Restaura el contenido de los campos de contraseña según sea necesario
        if (passwordVisible) {
            // Si el campo de contraseña visible está visible, restaura su contenido
            fieldContrasenaVisible.setText(passwordText);
        } else {
            // Si el campo de contraseña está visible, restaura su contenido
            fieldContrasena.setText(visiblePasswordText);
        }

        // Cambia el estado de passwordVisible
        passwordVisible = !passwordVisible;

        // Cambia el icono del ojo
        Image image = new Image(getClass().getResource(passwordVisible ? "/com/ui/img/loginPane/eyeOpen.png" : "/com/ui/img/loginPane/eyeClose.png").toExternalForm());
        eyeIcon.setImage(image);

        // Solicita el foco para el campo seleccionado
        selectedField.requestFocus();
    }







}
