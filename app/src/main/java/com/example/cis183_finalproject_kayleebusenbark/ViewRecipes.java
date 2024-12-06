package com.example.cis183_finalproject_kayleebusenbark;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ViewRecipes extends AppCompatActivity {

    private ListView listView;
    private DatabaseHelper dbHelper;
    ArrayAdapter<String> recipeCategoryAdapter;

    RecipeListAdaptor recipeListAdaptor;

    Spinner sp_j_viewAllRecipes_recipeCatSpinner;

    ImageButton im_j_searchButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_recipes);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.recipeSelector);
        setSupportActionBar(toolbar);

        sp_j_viewAllRecipes_recipeCatSpinner = findViewById(R.id.sp_v_allRecipes_Categories);
        listView = findViewById(R.id.lv_viewRecipes);
        im_j_searchButton = findViewById(R.id.imbtn_viewRecipes_search);
        dbHelper = new DatabaseHelper(this);

        ArrayList<Recipe> recipes = dbHelper.getAllRecipes();
        recipeListAdaptor = new RecipeListAdaptor(this, recipes);
        listView.setAdapter(recipeListAdaptor);

        bottomNavBarSetUp();


        ArrayList<String> reciepeCategoryList = dbHelper.getAllRecipeCategoriesForSpinner();
        reciepeCategoryList.add(0, "Search by Recipe Category:");
        recipeCategoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, reciepeCategoryList);
        sp_j_viewAllRecipes_recipeCatSpinner.setAdapter(recipeCategoryAdapter);

        listViewOnClickListener();
        filterRecipesOnClickListener();

    }

    private void filterRecipesOnClickListener()
    {
        im_j_searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterRecipesByCategory();
            }
        });
    }


    private void filterRecipesByCategory()
    {
        int selectedPosition = sp_j_viewAllRecipes_recipeCatSpinner.getSelectedItemPosition();

        if(selectedPosition > 0)
        {
            String selectedCategory = sp_j_viewAllRecipes_recipeCatSpinner.getSelectedItem().toString();
            int categoryId = dbHelper.getRecipeCategoryIdByName(selectedCategory);

            ArrayList<Recipe> filteredRecipes = dbHelper.getRecipesByCategoryId(categoryId);

            recipeListAdaptor = new RecipeListAdaptor(this, filteredRecipes);
            listView.setAdapter(recipeListAdaptor);
        }
        else
        {
            ArrayList<Recipe> recipes = dbHelper.getAllRecipes();
            recipeListAdaptor = new RecipeListAdaptor(this, recipes);
            listView.setAdapter(recipeListAdaptor);
        }
    }

    private void listViewOnClickListener()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Recipe selectedRecipe = (Recipe) recipeListAdaptor.getItem(i);

                SessionDataForRecipes.setSelectedRecipe(selectedRecipe);
                startActivity(new Intent(ViewRecipes.this, RecipeDetails.class));
            }
        });
    }

    private void bottomNavBarSetUp()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_recipeBook);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_pantry) {
                startActivity(new Intent(getApplicationContext(), Pantry.class));
                return true;
            } else if (itemId == R.id.bottom_addRecipe) {
                startActivity(new Intent(getApplicationContext(), AddRecipe.class));
                return true;
            } else if (itemId == R.id.bottom_recipeBook) {
                startActivity(new Intent(getApplicationContext(), ViewRecipes.class));

                return true;
            } else if (itemId == R.id.bottom_profile) {
                startActivity(new Intent(getApplicationContext(), UserInfo.class));
                return true;
            }
            return false;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.recipe_selector_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.myfavorites)
        {
            startActivity(new Intent(this, MyFavorites.class));
        }
        else if (item.getItemId() == R.id.myrecipes)
        {
            startActivity(new Intent(this, MyRecipes.class));
        }
        return super.onOptionsItemSelected(item);
    }
}