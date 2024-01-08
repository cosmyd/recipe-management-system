package com.jfxbase.recipesystem.controllers;

import com.jfxbase.recipesystem.JFXBaseApplication;
import com.jfxbase.recipesystem.utils.DBStuff;
import com.jfxbase.recipesystem.utils.Ingredient;
import com.jfxbase.recipesystem.utils.RecipeIngredient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class AddRecipeController {
    public int userId;
    public TextField unitTextField;
    public TextField quantityTextField;
    public Button saveRecipeButton;
    public VBox addedIngredients;
    public TextField recipeNameTextField;
    public ComboBox<Ingredient> ingredientComboBox;
    public TextArea recipeDescriptionTextArea;
    public Button addIngredientButton;
    public Button backButton;
    private ArrayList<RecipeIngredient> recipeIngredients;

    private Parent root;
    private Stage stage;
    private Scene scene;

    public void onAddIngredientButtonClick(ActionEvent event) {
        Ingredient selectedIngredient = ingredientComboBox.getValue();
        int quantity = Integer.parseInt(quantityTextField.getText());
        String unit = unitTextField.getText();
        RecipeIngredient recipeIngredient = new RecipeIngredient(selectedIngredient.getId(), selectedIngredient.getName(), selectedIngredient.getType(), quantity, unit);
        recipeIngredients.add(recipeIngredient);
        addedIngredients.getChildren().add(new TextField(recipeIngredient.getName() + " " + recipeIngredient.getQuantity()+ " " + recipeIngredient.getUnit()));
    }

    public void initialize(int userId) {
        DBStuff dbStuff = new DBStuff();
        Connection conn = dbStuff.connectDB();
        ArrayList<Ingredient> ingredients = dbStuff.getAllIngredients(conn);
        ObservableList<Ingredient> ingredientObservableList = FXCollections.observableArrayList(ingredients);
        this.userId = userId;
        recipeIngredients = new ArrayList<RecipeIngredient>();
        ingredientComboBox.setItems(ingredientObservableList);

        ingredientComboBox.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Ingredient ingredient, boolean empty) {
                super.updateItem(ingredient, empty);
                if (empty || ingredient == null) {
                    setText(null);
                } else {
                    setText(ingredient.getName());
                }
            }
        });
    }

    public void onSaveRecipeButtonClick(ActionEvent event) throws SQLException, IOException {
        DBStuff dbStuff = new DBStuff();
        Connection conn = dbStuff.connectDB();
        int recipeId = dbStuff.addRecipe(conn, this.userId, recipeNameTextField.getText(), recipeDescriptionTextArea.getText());
        dbStuff.addRecipeIngredients(conn, recipeIngredients, recipeId);
        FXMLLoader loader = new FXMLLoader(JFXBaseApplication.class.getResource("recipes-view.fxml"));
        root = loader.load();
        RecipesView recipesView = loader.getController();
        recipesView.initialize(this.userId);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

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
}
