package com.jfxbase.recipesystem.controllers;

import com.jfxbase.recipesystem.JFXBaseApplication;
import com.jfxbase.recipesystem.utils.DBStuff;
import com.jfxbase.recipesystem.utils.Recipe;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EditRecipeController {
    public Button backButton;
    public Button saveRecipe;
    public TextField recipeNameTextField;
    public TextArea recipeDescriptionTextArea;
    private int userId;
    private int recipeId;
    private ArrayList<RecipeIngredient> recipeIngredients;
    private ArrayList<RecipeIngredient> removedIngredients;
    private Recipe recipe;

    private Parent root;
    private Stage stage;
    private Scene scene;

    @FXML
    private TableView<RecipeIngredient> ingredientTable;

    @FXML
    private TableColumn<RecipeIngredient, String> ingredientColumn;

    @FXML
    private TableColumn<RecipeIngredient, Integer> quantityColumn;

    @FXML
    private TableColumn<RecipeIngredient, String> unitColumn;
    public TableColumn <RecipeIngredient, Void> deleteColumn;

    public void initialize(int userId, int recipeId) {
        DBStuff dbStuff = new DBStuff();
        Connection conn = dbStuff.connectDB();
        System.out.println(userId + " " + recipeId);
        recipeIngredients = dbStuff.getRecipeIngredients(conn, recipeId);
        removedIngredients = new ArrayList<>();
        String recipeName = null;
        String recipeDescription = null;
        try {
            ResultSet resultSet = dbStuff.getById(conn, "recipes", recipeId);
            while (resultSet.next()) {
                recipeName = resultSet.getString("name");
                recipeDescription = resultSet.getString("description");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        recipeNameTextField.setText(recipeName);
        recipeDescriptionTextArea.setText(recipeDescription);

        ObservableList<RecipeIngredient> ingredientObservableList = FXCollections.observableArrayList(recipeIngredients);
        this.userId = userId;
        this.recipeId = recipeId;
        ingredientColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unit"));
        ingredientTable.setItems(ingredientObservableList);


        ingredientTable.setEditable(true);
        ingredientTable.getSelectionModel().cellSelectionEnabledProperty().set(true);
        ingredientTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                ingredientTable.edit(ingredientTable.getSelectionModel().getSelectedIndex(), quantityColumn);
            }
        });

        quantityColumn.setEditable(true);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityColumn.setOnEditCommit(event -> {
            RecipeIngredient ingredient = event.getRowValue();
            ingredient.setQuantity(event.getNewValue());
        });
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Delete");

            {
                deleteButton.setOnAction(event -> {
                    RecipeIngredient ingredient = getTableView().getItems().get(getIndex());
                    removedIngredients.add(ingredient);
                    ingredientTable.getItems().remove(ingredient);

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

    public void onBackButtonClick(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(JFXBaseApplication.class.getResource("home-view.fxml"));
        root = loader.load();
        HomeView homeView = loader.getController();
        homeView.initialize(this.userId);
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onSaveRecipeButtonClick(ActionEvent event) throws SQLException {
        DBStuff dbStuff = new DBStuff();
        Connection conn = dbStuff.connectDB();

        dbStuff.updateRecipeIngredients(conn, recipeIngredients, removedIngredients, recipeId);
    }

    //TODO add the description field to the recipe, and the functionalities for it as well, shopping list and it should be done
}
