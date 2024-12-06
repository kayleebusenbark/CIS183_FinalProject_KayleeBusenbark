package com.example.cis183_finalproject_kayleebusenbark;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MyFavorites extends AppCompatActivity
{
    ListView favoritesListView;
    RecipeListAdaptor adaptor;
    ArrayList<Recipe> favoriteRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_favorites);

        favoritesListView = findViewById(R.id.lv_viewMyFavoriteRecipes);

        bottomNavBarSetUp();

        loadFavoriteRecipes();
        listViewOnClickListener();

    }

    private void loadFavoriteRecipes()
    {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        favoriteRecipes = dbHelper.getFavoriteRecipesByUserId(SessionData.getLoggedInUser().getUserId());

        adaptor = new RecipeListAdaptor(this, favoriteRecipes);
        favoritesListView.setAdapter(adaptor);
    }

    private void listViewOnClickListener()
    {
        favoritesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Recipe selectedRecipe = (Recipe) favoriteRecipes.get(i);

                SessionDataForRecipes.setSelectedRecipe(selectedRecipe);
                startActivity(new Intent(MyFavorites.this, RecipeDetails.class));
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
}