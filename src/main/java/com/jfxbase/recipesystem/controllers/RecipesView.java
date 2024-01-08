package com.jfxbase.recipesystem.controllers;

import com.jfxbase.recipesystem.JFXBaseApplication;
import com.jfxbase.recipesystem.utils.DBStuff;
import com.jfxbase.recipesystem.utils.Recipe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

public class RecipesView {
    public Button addRecipeButton;
    int userId;
    private Parent root;
    private Stage stage;
    private Scene scene;
    @FXML
    protected VBox recipesVBox;

    @FXML
    protected void onBackButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(JFXBaseApplication.class.getResource("home-view.fxml"));
        root = loader.load();
        HomeView homeView = loader.getController();
        homeView.initialize(this.userId);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



    public void initialize(int userId) {
        DBStuff dbStuff = new DBStuff();
        Connection conn = dbStuff.connectDB();
        ArrayList<Recipe> recipes = dbStuff.getUserRecipes(conn, userId);
        ObservableList<Recipe> recipeObservableList = FXCollections.observableArrayList(recipes);
        this.userId = userId;
        for(Recipe recipe : recipes){
            Button button = new Button(recipe.getName());
            button.setOnAction(event -> {
                try {
                    recipeDetailsClick(event, recipe.getId());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            recipesVBox.getChildren().add(button);
        }
    }

    public void recipeDetailsClick(ActionEvent event, int recipeId) throws IOException {
        FXMLLoader loader = new FXMLLoader(JFXBaseApplication.class.getResource("recipe-detail-view.fxml"));
        root = loader.load();
        RecipeDetailView recipeDetailView = loader.getController();
        recipeDetailView.initialize(this.userId, recipeId);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    protected void onAddRecipeButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(JFXBaseApplication.class.getResource("add-recipe-view.fxml"));
        root = loader.load();
        AddRecipeController addRecipeController = loader.getController();
        addRecipeController.initialize(this.userId);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

}
