package com.example.cis183_finalproject_kayleebusenbark;

import java.io.Serializable;

public class RecipeIngredient implements Serializable
{
    private int recipeId;
    private int ingredientId;
    private float ingredientQuantity;
    private int measurementId;

    public RecipeIngredient()
    {}

    public RecipeIngredient(int recipeId, int ingredientId, float ingredientQuantity, int measurementId)
    {
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.ingredientQuantity = ingredientQuantity;
        this.measurementId = measurementId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }

    public float getIngredientQuantity() {
        return ingredientQuantity;
    }

    public void setIngredientQuantity(float ingredientQuantity) {
        this.ingredientQuantity = ingredientQuantity;
    }

    public int getMeasurementId() {
        return measurementId;
    }

    public void setMeasurementId(int measurementId) {
        this.measurementId = measurementId;
    }
    
}
