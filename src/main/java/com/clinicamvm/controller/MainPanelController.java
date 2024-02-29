package com.clinicamvm.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class MainPanelController {
    private MainPanelController loginController;

    public void setLoginController(MainPanelController loginController) {
        this.loginController = loginController;
    }

    @FXML
    private void cargarMainPanel() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ui/mainPanel.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 820, 640);

            // Obtener el controlador del MainPanel
            MainPanelController controller = fxmlLoader.getController();
            controller.setLoginController(this.loginController);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
