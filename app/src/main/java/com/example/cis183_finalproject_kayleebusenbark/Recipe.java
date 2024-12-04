package com.example.cis183_finalproject_kayleebusenbark;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Recipe implements Serializable
{
    private int recipeId;
    private int userId;
    private String recipeTitle;
    private String recipeInstructions;
    private float recipePrepTime;
    private String prepTimeCat;
    private int recipeCatId;

    public Recipe(int recipeId, int userId, String recipeTitle, String recipeInstructions, float recipePrepTime, String prepTimeCat, int recipeCatId)
    {
        this.recipeId = recipeId;
        this.userId = userId;
        this.recipeTitle = recipeTitle;
        this.recipeInstructions = recipeInstructions;
        this.recipePrepTime = recipePrepTime;
        this.prepTimeCat = prepTimeCat;
        this.recipeCatId = recipeCatId;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public int getRecipeCatId() {
        return recipeCatId;
    }

    public void setRecipeCatId(int recipeCatId) {
        this.recipeCatId = recipeCatId;
    }

    public String getPrepTimeCat() {
        return prepTimeCat;
    }

    public void setPrepTimeCat(String prepTimeCat) {
        this.prepTimeCat = prepTimeCat;
    }

    public float getRecipePrepTime() {
        return recipePrepTime;
    }

    public void setRecipePrepTime(float recipePrepTime) {
        this.recipePrepTime = recipePrepTime;
    }

    public String getRecipeInstructions() {
        return recipeInstructions;
    }

    public void setRecipeInstructions(String recipeInstructions) {
        this.recipeInstructions = recipeInstructions;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    static class prepTimeCategory
    {
        static ArrayList<String> prepTimeCategories = new ArrayList<>(Arrays.asList("Time Amount", "minute(s)", "hour(s)"));

        public static ArrayList<String> getAllRanges()
        {
            return prepTimeCategories;
        }
        public static String getRangeAt(int i)
        {
            return prepTimeCategories.get(i);
        }


    }

}
