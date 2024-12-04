package com.example.cis183_finalproject_kayleebusenbark;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class ViewRecipes extends AppCompatActivity {

    private ListView listView;
    private DatabaseHelper dbHelper;
    ArrayAdapter<String> recipeCategoryAdapter;

    RecipeListAdaptor recipeListAdaptor;

    Spinner sp_j_viewAllRecipes_recipeCatSpinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_recipes);

        sp_j_viewAllRecipes_recipeCatSpinner = findViewById(R.id.sp_v_allRecipes_Categories);
        listView = findViewById(R.id.lv_viewRecipes);
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
        getMenuInflater().inflate(R.menu.recipe_menu_selector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.myRecipes)
        {
            startActivity(new Intent(ViewRecipes.this, MyRecipes.class));
        }
        else if (item.getItemId() == R.id.myFavorites)
        {
            startActivity(new Intent(ViewRecipes.this, MyFavorites.class));
        }
        else
        {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }


}