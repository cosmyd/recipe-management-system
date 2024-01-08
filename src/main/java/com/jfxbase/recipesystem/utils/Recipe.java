package com.jfxbase.recipesystem.utils;

import java.util.ArrayList;

public class Recipe {
    private int id;
    private String name;
    private ArrayList<RecipeIngredient> recipeIngredients;


    public Recipe(int id, String name, ArrayList<RecipeIngredient> recipeIngredients) {
        this.id = id;
        this.name = name;
        this.recipeIngredients = recipeIngredients;
    }
    public Recipe(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public void addIngredient(RecipeIngredient recipeIngredient){
        recipeIngredients.add(recipeIngredient);
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<RecipeIngredient> getIngredients() {
        return recipeIngredients;
    }

    public void setIngredients(ArrayList<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public RecipeIngredient getIngredientById(int ingredientId){
        for(RecipeIngredient ingredient : recipeIngredients) {
            if(ingredient.getId() == ingredientId){
                return ingredient;
            }
        }
        return null;
    }


}
