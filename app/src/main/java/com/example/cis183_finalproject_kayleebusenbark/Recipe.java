package com.example.cis183_finalproject_kayleebusenbark;

import java.io.Serializable;

public class Recipe implements Serializable
{
    private int recipeId;
    private String recipeTitle;
    private float recipePrepTime;
    private String recipeInstructions;

    public Recipe(int recipeId, String recipeTitle, float recipePrepTime, String recipeInstructions)
    {
        recipeId = recipeId;
        recipeTitle = recipeTitle;
        recipePrepTime = recipePrepTime;
        recipeInstructions = recipeInstructions;
    }
    //GETTERS
    public int getRecipeId() {
        return recipeId;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public float getRecipePrepTime() {
        return recipePrepTime;
    }

    public String getRecipeInstructions() {
        return recipeInstructions;
    }
    //SETTERS

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public void setRecipePrepTime(float recipePrepTime) {
        this.recipePrepTime = recipePrepTime;
    }

    public void setRecipeInstructions(String recipeInstructions) {
        this.recipeInstructions = recipeInstructions;
    }
}
