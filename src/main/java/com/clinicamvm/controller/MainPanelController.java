package com.clinicamvm.controller;

import business.entities.Personal;
import persistence.daos.contracts.PersonalDAO;

public class MainPanelController {
    private LoginController loginController;
    private PersonalDAO personalDAO; // DAO para acceder a los datos del personal
    private Personal loggedInUser; // Usuario que ha iniciado sesión

    public void setLoginController(LoginController loginController) {
        this.loginController = loginController;
    }

    public void setPersonalDAO(PersonalDAO personalDAO) {
        this.personalDAO = personalDAO;
    }

    public Personal getLoggedInUser() {
        return loggedInUser;
    }

    public void setUserAfterLogin(String username) {




    }

    // Otros métodos y lógica de tu controlador
}
