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

public class LogInController {
    public TextField usernameField;
    public PasswordField passwordField;
    public Button signUpButton;
    public Button logInButton;

    private Parent root;
    private Stage stage;
    private Scene scene;

    public void onLogInButtonClick(ActionEvent event) throws IOException, SQLException {
        FXMLLoader loader = new FXMLLoader(JFXBaseApplication.class.getResource("home-view.fxml"));
        String username = usernameField.getText();
        String password = passwordField.getText();
        DBStuff dbStuff = new DBStuff();
        Connection conn = dbStuff.connectDB();
        ResultSet resultSet = dbStuff.getByUsername(conn, username);
        int userId = 0;
        if (resultSet.next()){
            userId = resultSet.getInt("id");
            String correctPassword = resultSet.getString("password");
            if (password.equals(correctPassword)){
                root = loader.load();
                HomeView homeView = loader.getController();
                homeView.initialize(userId);
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Password incorrect");

                alert.setContentText("Password incorrect");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK){
                    this.initialize();
                } else {
                    this.initialize();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("User does not exit");

            alert.setContentText("User does not exits");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK){
                this.initialize();
            } else {
                this.initialize();
            }
        }

    }

    public void initialize(){

    }

    public void onSignUpButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(JFXBaseApplication.class.getResource("sign-up-view.fxml"));
        root = loader.load();
        SignUpController signUpController = loader.getController();
        signUpController.initialize();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
