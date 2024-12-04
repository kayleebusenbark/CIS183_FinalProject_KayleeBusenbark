package com.example.cis183_finalproject_kayleebusenbark;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SearchRecipeListAdaptor extends BaseAdapter
{
    Context context;

    ArrayList<RecipeMatch> listOfRecipeMatches;

    DatabaseHelper dbHelper;


    public SearchRecipeListAdaptor(Context c, ArrayList<RecipeMatch> ls)
    {
        context = c;
        listOfRecipeMatches = ls;
    }


    @Override
    public int getCount() {
        return listOfRecipeMatches.size();
    }

    @Override
    public Object getItem(int i) {
        return listOfRecipeMatches.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        if(view == null)
        {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(ViewRecipes.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(R.layout.searchrecipes_custom_cell, null);
        }
        TextView recipeTitle = view.findViewById(R.id.tv_v_searchRecipes_cc_recipeTitle);
        TextView ingredientCount = view.findViewById(R.id.tv_v_searchRecipes_cc_ingredientCount);
        TextView prepTime = view.findViewById(R.id.tv_v_searchRecipes_cc_prepTime);
        dbHelper = new DatabaseHelper(context);


        RecipeMatch recipeMatch = listOfRecipeMatches.get(i);
        Recipe recipe = recipeMatch.getRecipe();

        recipeTitle.setText(recipe.getRecipeTitle());


        String prepTimeText;
        if(recipe.getRecipePrepTime() % 1 == 0)
        {
            prepTimeText = String.valueOf((int) recipe.getRecipePrepTime());
        }
        else
        {
            prepTimeText = String.valueOf(recipe.getRecipePrepTime());
        }

        prepTimeText += " " + recipe.getPrepTimeCat();
        prepTime.setText(prepTimeText);

        ingredientCount.setText("Ingredients: " + recipeMatch.getMatchedIngredient() + "/" + recipeMatch.getTotalIngredients());



        return view;
    }
}
