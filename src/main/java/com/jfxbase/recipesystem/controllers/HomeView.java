package com.jfxbase.recipesystem.controllers;

import com.jfxbase.recipesystem.JFXBaseApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeView{
    /*@FXML
    protected void onMyRecipesButtonClick(){

        this.changeScene(SCENE_IDENTIFIER.RECIPES);
    }*/
    public int userId;
    public Button shoppingListButton;
    private Parent root;
    private Stage stage;
    private Scene scene;

    @FXML
    protected void onLogOutButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(JFXBaseApplication.class.getResource("log-in-view.fxml"));
        root = loader.load();
        LogInController logInController = loader.getController();
        logInController.initialize();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void initialize(int userId) {
        this.userId = userId;
    }


    public void onMyRecipesButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(JFXBaseApplication.class.getResource("recipes-view.fxml"));
        root = loader.load();
        RecipesView recipesView = loader.getController();
        recipesView.initialize(this.userId);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onShoppingListButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(JFXBaseApplication.class.getResource("shopping-list-view.fxml"));
        root = loader.load();
        ShoppingListController shoppingListController = loader.getController();
        shoppingListController.initialize(this.userId);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
