package com.example.cis183_finalproject_kayleebusenbark;

public class SessionDataForRecipes
{
    private static Recipe selectedRecipe;

    public static Recipe getSelectedRecipe()
    {
        return  selectedRecipe;
    }

    public static void setSelectedRecipe(Recipe r)
    {
        selectedRecipe = r;
    }


}
