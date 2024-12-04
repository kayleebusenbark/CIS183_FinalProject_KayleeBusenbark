package com.example.cis183_finalproject_kayleebusenbark;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RecipeListAdaptor extends BaseAdapter
{
    Context context;

    ArrayList<Recipe> listOfRecipes;

    DatabaseHelper dbHelper;


    public RecipeListAdaptor(Context c, ArrayList<Recipe> ls)
    {
        context = c;
        listOfRecipes = ls;
    }


    @Override
    public int getCount() {
        return listOfRecipes.size();
    }

    @Override
    public Object getItem(int i) {
        return listOfRecipes.get(i);
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
            view = mInflater.inflate(R.layout.recipe_custom_cell, null);
        }
        TextView recipeTitle = view.findViewById(R.id.tv_v_recipes_cc_recipeTitle);
        TextView writtenBy = view.findViewById(R.id.tv_v_recipes_cc_createdBy);
        TextView prepTime = view.findViewById(R.id.tv_v_recipes_cc_prepTime);
        dbHelper = new DatabaseHelper(context);


        Recipe recipe = listOfRecipes.get(i);

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

        if (recipe.getUserId() == SessionData.getLoggedInUser().getUserId())
        {
            writtenBy.setText("Created By: You");

        }
        else
        {
            User user = dbHelper.getUserById(recipe.getUserId());

            String createdByText = "Created By: " + user.getFname() + " " + user.getLname();
            writtenBy.setText(createdByText);
        }



        return view;
    }
}
