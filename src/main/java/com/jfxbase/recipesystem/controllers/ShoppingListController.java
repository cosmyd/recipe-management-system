package com.jfxbase.recipesystem.controllers;

import com.jfxbase.recipesystem.JFXBaseApplication;
import com.jfxbase.recipesystem.utils.DBStuff;
import com.jfxbase.recipesystem.utils.RecipeIngredient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

public class ShoppingListController {
    public Button backButton;
    @FXML
    private TableView<RecipeIngredient> ingredientTable;

    @FXML
    private TableColumn<RecipeIngredient, String> ingredientColumn;

    @FXML
    private TableColumn<RecipeIngredient, Integer> quantityColumn;

    @FXML
    private TableColumn<RecipeIngredient, String> unitColumn;
    public TableColumn <RecipeIngredient, Void> boughtColumn;

    private Parent root;
    private Stage stage;
    private Scene scene;

    private ArrayList<RecipeIngredient> shopingList;
    private int userId;

    public void initialize(int userId) {
        DBStuff dbStuff = new DBStuff();
        Connection conn = dbStuff.connectDB();
        shopingList = dbStuff.getShoppingList(conn, userId);
        ObservableList<RecipeIngredient> ingredientObservableList = FXCollections.observableArrayList(shopingList);
        this.userId = userId;
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


        boughtColumn.setCellFactory(param -> new TableCell<>() {
            private final Button deleteButton = new Button("Bought");

            {
                deleteButton.setOnAction(event -> {
                    RecipeIngredient ingredient = getTableView().getItems().get(getIndex());
                    dbStuff.deleteFromShoppingList(conn, userId, ingredient.getId());
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
}
