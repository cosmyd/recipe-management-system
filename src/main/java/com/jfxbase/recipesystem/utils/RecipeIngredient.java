package com.jfxbase.recipesystem.utils;

public class RecipeIngredient extends Ingredient{
    private int quantity;
    private String unit;


    public RecipeIngredient(int quantity, String unit) {
        this.quantity = quantity;
        this.unit = unit;
    }

    public RecipeIngredient(int id, String name, String type, int quantity, String unit) {
        super(id, name, type);
        this.quantity = quantity;
        this.unit = unit;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
