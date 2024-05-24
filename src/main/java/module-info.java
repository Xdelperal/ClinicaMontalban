module com.clinicamvm.clinicamontalban {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires com.jfoenix;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.desktop;

    opens business.entities to javafx.base;
    opens com.clinicamvm.controller to javafx.fxml;
    exports com.clinicamvm.controller;
    exports app;
    opens app to javafx.fxml;
}
