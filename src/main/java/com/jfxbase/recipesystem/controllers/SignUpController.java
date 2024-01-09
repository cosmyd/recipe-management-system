package com.jfxbase.recipesystem.controllers;

import com.jfxbase.recipesystem.JFXBaseApplication;
import com.jfxbase.recipesystem.utils.DBStuff;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class SignUpController {
    public Button SignUpButton;
    public Button LogInButton;
    public TextField usernameField;
    public PasswordField passwordField;

    private Parent root;
    private Stage stage;
    private Scene scene;

    public void initialize(){

    }

    public void onSignUpButtonClick(ActionEvent event) throws SQLException, IOException {
        FXMLLoader loader = new FXMLLoader(JFXBaseApplication.class.getResource("home-view.fxml"));
        String username = usernameField.getText();
        String password = passwordField.getText();
        DBStuff dbStuff = new DBStuff();
        Connection conn = dbStuff.connectDB();
        ResultSet resultSet = dbStuff.getByUsername(conn, username);
        if (resultSet.next()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Username already exists");
            alert.setContentText("Username already exists");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                this.initialize();
            } else {
                this.initialize();
            }

        } else {
            dbStuff.addUser(conn, username, password);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("User added");

            alert.setContentText("User added succesfully!");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                this.initialize();
            } else {
                this.initialize();
            }
        }
    }

    public void onLogInButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(JFXBaseApplication.class.getResource("log-in-view.fxml"));
        root = loader.load();
        LogInController logInController = loader.getController();
        logInController.initialize();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
