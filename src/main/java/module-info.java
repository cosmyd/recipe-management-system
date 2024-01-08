module com.jfxbase.oopjfxbase {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires org.junit.jupiter.api;

    opens com.jfxbase.recipesystem to javafx.fxml;
    exports com.jfxbase.recipesystem;
    exports com.jfxbase.recipesystem.utils;
    opens com.jfxbase.recipesystem.utils to javafx.fxml;
    exports com.jfxbase.recipesystem.controllers;
    opens com.jfxbase.recipesystem.controllers to javafx.fxml;
}