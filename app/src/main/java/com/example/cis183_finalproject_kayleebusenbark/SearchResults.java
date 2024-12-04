package com.example.cis183_finalproject_kayleebusenbark;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class SearchResults extends AppCompatActivity
{
    private DatabaseHelper dbHelper;
    private ListView lvRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search_results);

        lvRecipes = findViewById(R.id.lv_viewRecipes);

        dbHelper = new DatabaseHelper(this);

        int userId = SessionData.getLoggedInUser().getUserId();
        ArrayList<RecipeMatch> recipeMatches = dbHelper.getMatchingRecipes(userId);

        SearchRecipeListAdaptor adaptor = new SearchRecipeListAdaptor(this, recipeMatches);
        lvRecipes.setAdapter(adaptor);

        lvRecipes.setOnItemClickListener((parent, view, position, id) -> {
            Recipe selectedRecipe = recipeMatches.get(position).getRecipe();
            SessionDataForRecipes.setSelectedRecipe(selectedRecipe);
            startActivity(new Intent(SearchResults.this, RecipeDetails.class));
        });

        bottomNavBarSetUp();
    }

    private void bottomNavBarSetUp()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
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


