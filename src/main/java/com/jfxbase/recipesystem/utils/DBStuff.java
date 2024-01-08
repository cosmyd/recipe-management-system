package com.jfxbase.recipesystem.utils;

import java.sql.*;
import java.util.ArrayList;

public class DBStuff {

    public Connection connectDB(){
        Connection conn = null;
        try{
            Class.forName("org.postgresql.Driver");
            conn = DriverManager.getConnection(Environment.DB_URL, Environment.DB_USER, Environment.DB_USER_PASSWORD);
            if(conn != null){
                System.out.println("Connection established!");
            } else {
                System.out.println("Connection failed!");
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return conn;
    }
    //create(table, obj);
    public void insertRow(Connection conn, String table, String values){
        Statement statement;
        try{
            String query = "INSERT INTO " + table + " VALUES (" + values +")";
            statement = conn.createStatement();
            statement.executeUpdate(query);
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public ArrayList<Recipe> getUserRecipes(Connection conn, Integer userId){
        Statement statement;
        ResultSet resultSet = null;
        ArrayList<Recipe> recipes = new ArrayList<>();
        try{
            String query = "SELECT * FROM public.recipes "+ " WHERE user_id = " + userId.toString();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()){
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Recipe recipe = new Recipe(id, name);
                System.out.println("Recipe " + recipe.getName() +" added");
                recipes.add(recipe);
            }
        } catch (Exception e){
            System.out.println(e);
        }

        return recipes;
    }


    public ArrayList<RecipeIngredient> getRecipeIngredients(Connection conn, Integer recipeId){
        Statement statement;
        ResultSet resultSet = null;
        ArrayList<RecipeIngredient> recipeIngredients = new ArrayList<>();
        try{
            String query = "SELECT * FROM public.recipeingredients "+ " WHERE recipe_id = " + recipeId.toString();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                int ingredientId = resultSet.getInt("ingredient_id");
                int quantity = resultSet.getInt("quantity");
                String unit = resultSet.getString("unit");
                String ingredientName = null;
                String type = null;
                ResultSet rs = getById(conn, "ingredients", ingredientId);
                while(rs.next()){
                    ingredientName = rs.getString("name");
                    type = rs.getString("type");
                }
                RecipeIngredient recipeIngredient = new RecipeIngredient(ingredientId, ingredientName, type,quantity,unit);
                recipeIngredients.add(recipeIngredient);
            }
        } catch (Exception e){
            System.out.println(e);
        }
        return recipeIngredients;
    }

    public ResultSet getById(Connection conn, String table, Integer id){
        Statement statement;
        ResultSet resultSet = null;
        try{
            String query = "SELECT * FROM " + table + " WHERE id = " + id.toString();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (Exception e){
            System.out.println(e);
        }
        return resultSet;
    }
    public ResultSet getByName(Connection conn, String table, String name){
        Statement statement;
        ResultSet resultSet = null;
        try{
            String query = "SELECT * FROM " + table + " WHERE name = '" + name + "'";
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (Exception e){
            System.out.println(e);
        }
        return resultSet;
    }

    public ResultSet getByUsername(Connection conn, String username){
        Statement statement;
        ResultSet resultSet = null;
        try{
            String query = "SELECT * FROM users WHERE username = '" + username + "'";
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (Exception e){
            System.out.println(e);
        }
        return resultSet;
    }

    public ArrayList<Ingredient> getAllIngredients(Connection conn){
        Statement statement;
        ResultSet resultSet = null;
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        try{
            String query = "select * from public.ingredients";
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                String ingredientName = resultSet.getString("name");
                String type = resultSet.getString("type");
                Ingredient ingredient = new Ingredient(id, ingredientName, type);
                ingredients.add(ingredient);
            }
        } catch (Exception e){
            System.out.println(e);
        }
        return ingredients;
    }

    public void addUser(Connection conn, String username, String password) throws SQLException {
        String values = String.format("'%s', '%s'", username, password);
        insertRow(conn, "users (username, password)", values);
    }

    public int addRecipe(Connection conn, int userId, String name, String description) throws SQLException {
        String values = String.valueOf(userId) + ", '" + name + "', '" + description + "'";
        insertRow(conn, "recipes (user_id, name, description)", values);
        ResultSet rs = getByName(conn, "recipes", name);
        int recipeId = -1;
        while(rs.next()){
            recipeId = rs.getInt("id");
        }
        return recipeId;

    }

    public void addRecipeIngredients(Connection conn, ArrayList<RecipeIngredient> ingredients, int recipeId){
        for(RecipeIngredient ingredient : ingredients){
            String values = String.format("%d, %d, %d, '%s'", recipeId, ingredient.getId(), ingredient.getQuantity(),
                    ingredient.getUnit());
            insertRow(conn, "recipeingredients (recipe_id, ingredient_id, quantity, unit)", values);
        }
    }

    public void updateRecipeIngredients(Connection conn, ArrayList<RecipeIngredient> ingredients, ArrayList<RecipeIngredient> removedIngredients, int recipeId) {
        for (RecipeIngredient ingredient : ingredients) {
            int ingredientId = ingredient.getId();
            int ingredientQuantity = ingredient.getQuantity();
            String ingredientUnit = ingredient.getUnit();
            String query = String.format("SELECT COUNT(*) FROM recipeingredients WHERE ingredient_id = %d AND recipe_id = %d", ingredientId, recipeId);

            Statement statement;
            try {
                statement = conn.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                resultSet.next();
                int count = resultSet.getInt(1);
                if (count > 0) {
                    String updateQuery = String.format("UPDATE recipeingredients SET quantity = %d, unit = '%s' WHERE ingredient_id = %d AND recipe_id = %d", ingredientQuantity, ingredientUnit, ingredientId,recipeId);
                    try {
                        Statement updateStatement = conn.createStatement();
                        updateStatement.executeUpdate(updateQuery);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    String values = String.format("%d, %d, '%s', %d", ingredientId, recipeId, ingredientUnit, ingredientQuantity);
                    //insertRow(conn, "recipeingredients (ingredient_id, recipe_id, unit, quantity)", values);
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        for (RecipeIngredient ingredient : removedIngredients ){
            deleteIngredient(conn, recipeId, ingredient.getId());
        }
    }

    public void updateRecipe(Connection conn, int recipeId, String name, String description) {
        String updateQuery = String.format("UPDATE recipe SET name = %s, description = &s WHERE recipe_id = %d", name, description, recipeId);
        try{
            Statement statement = conn.createStatement();
            statement.executeUpdate(updateQuery);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    //delete(table, id);
    public void deleteRecipe(Connection conn, int recipeId){
        String deleteQuery1 = String.format("DELETE FROM recipeingredients WHERE recipe_id = %d", recipeId);
        String deleteQuery2 = String.format("DELETE FROM recipes WHERE id = %d", recipeId);
        try{
            Statement statement = conn.createStatement();
            statement.executeUpdate(deleteQuery1);
            statement.executeUpdate(deleteQuery2);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteIngredient(Connection conn, int recipeId, int ingredientId){
        String deleteQuery = String.format("DELETE FROM recipeingredients WHERE recipe_id = %d and ingredient_id = %d", recipeId, ingredientId);
        try{
            Statement statement = conn.createStatement();
            statement.executeUpdate(deleteQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFromShoppingList(Connection conn, int userId, int ingredientId){
        String deleteQuery = String.format("DELETE FROM shoppinglistitems WHERE user_id = %d and ingredient_id = %d", userId, ingredientId);
        try{
            Statement statement = conn.createStatement();
            statement.executeUpdate(deleteQuery);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public ArrayList<RecipeIngredient> getShoppingList(Connection conn, Integer userId){
        Statement statement;
        ResultSet resultSet = null;
        ArrayList<RecipeIngredient> recipeIngredients = new ArrayList<>();
        try{
            String query = "SELECT * FROM public.shoppinglistitems "+ " WHERE user_id = " + userId.toString();
            statement = conn.createStatement();
            resultSet = statement.executeQuery(query);
            while(resultSet.next()) {
                int id = resultSet.getInt("id");
                int ingredientId = resultSet.getInt("ingredient_id");
                int quantity = resultSet.getInt("quantity");
                String unit = resultSet.getString("unit");
                String ingredientName = null;
                String type = null;
                ResultSet rs = getById(conn, "ingredients", ingredientId);
                while(rs.next()){
                    ingredientName = rs.getString("name");
                    type = rs.getString("type");
                }
                RecipeIngredient recipeIngredient = new RecipeIngredient(ingredientId, ingredientName, type,quantity,unit);
                recipeIngredients.add(recipeIngredient);
            }
        } catch (Exception e){
            System.out.println(e);
        }
        return recipeIngredients;
    }
}
