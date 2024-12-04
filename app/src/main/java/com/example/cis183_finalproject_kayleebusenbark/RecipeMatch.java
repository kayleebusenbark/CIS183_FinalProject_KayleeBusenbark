package com.example.cis183_finalproject_kayleebusenbark;

public class RecipeMatch
{
    private Recipe recipe;
    private int matchedIngredient;
    private int totalIngredients;

    public RecipeMatch(Recipe r, int mi, int ti)
    {
        this.recipe = r;
        this.matchedIngredient = mi;
        this.totalIngredients = ti;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public int getMatchedIngredient() {
        return matchedIngredient;
    }

    public void setMatchedIngredient(int matchedIngredient) {
        this.matchedIngredient = matchedIngredient;
    }

    public int getTotalIngredients() {
        return totalIngredients;
    }

    public void setTotalIngredients(int totalIngredients) {
        this.totalIngredients = totalIngredients;
    }
}
