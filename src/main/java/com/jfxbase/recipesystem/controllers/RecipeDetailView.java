package com.jfxbase.recipesystem.controllers;

import com.jfxbase.recipesystem.JFXBaseApplication;
import com.jfxbase.recipesystem.utils.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class RecipeDetailView {
    public Button deleteRecipeButton;
    public Text recipeNameText;
    public Text recipeDescriptionText;
    public Button backButton;
    public Button editRecipeButton;

    int userId;
    int recipeId;
    private Parent root;
    private Stage stage;
    private Scene scene;


    public TableView<RecipeIngredient> ingredientTable;
    public TableColumn<RecipeIngredient, String> ingredientColumn;
    public TableColumn<RecipeIngredient, Integer> quantityColumn;
    public TableColumn<RecipeIngredient, String> unitColumn;
    public TableColumn<RecipeIngredient, Void> addToSLColumn;



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


    public void initialize(int userId, int recipeId) {
        DBStuff dbStuff = new DBStuff();
        Connection conn = dbStuff.connectDB();
        ArrayList<RecipeIngredient> recipeIngredients = dbStuff.getRecipeIngredients(conn, recipeId);
        ObservableList<RecipeIngredient> ingredientObservableList = FXCollections.observableArrayList(recipeIngredients);
        this.userId = userId;
        this.recipeId = recipeId;
        String recipeName = null;
        String recipeDescription = null;
        try{
            ResultSet resultSet = dbStuff.getById(conn, "recipes", recipeId);
            while (resultSet.next()){
                recipeName = resultSet.getString("name");
                recipeDescription = resultSet.getString("description");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        recipeNameText.setText(recipeName);
        recipeDescriptionText.setText(recipeDescription);

        ingredientColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        ingredientTable.setItems(ingredientObservableList);
        addToSLColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Add");

            {
                deleteButton.setOnAction(event -> {
                    RecipeIngredient ingredient = getTableView().getItems().get(getIndex());
                    String values = String.format("%d, %d, %d, '%s'", userId, ingredient.getId(), ingredient.getQuantity(), ingredient.getUnit());
                    dbStuff.insertRow(conn, "shoppinglistitems (user_id, ingredient_id, quantity, unit)", values);
                    deleteButton.setText("Added");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });

    }



    public void onEditRecipeButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(JFXBaseApplication.class.getResource("edit-recipe-view.fxml"));
        root = loader.load();
        EditRecipeController editRecipeController = loader.getController();
        editRecipeController.initialize(this.userId, this.recipeId);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public void onDeleteRecipeButtonClick(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

        alert.setTitle("Delete Confirmation");
        alert.setHeaderText("Are you sure you want to delete this recipe?");
        alert.setContentText("Are you sure you want to delete this recipe?");
        Optional<ButtonType> result = alert.showAndWait();

        if(result.isPresent() && result.get() == ButtonType.OK){
            DBStuff dbStuff = new DBStuff();
            Connection conn = dbStuff.connectDB();
            dbStuff.deleteRecipe(conn, recipeId);
        } else {
            this.initialize(this.userId, this.recipeId);
            return;
        }

    }
}

