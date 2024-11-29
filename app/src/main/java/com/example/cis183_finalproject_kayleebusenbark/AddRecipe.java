package com.example.cis183_finalproject_kayleebusenbark;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class AddRecipe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_recipe);
        startActivity(new Intent(getApplicationContext(), ViewRecipes.class));

        //NAV BAR
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_addRecipe);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if(itemId == R.id.bottom_pantry)
            {
                startActivity(new Intent(getApplicationContext(), Pantry.class));
                return true;
            }
            else if(itemId == R.id.bottom_addRecipe)
            {
                return true;
            }
            else if(itemId == R.id.bottom_recipeBook)
            {
                startActivity(new Intent(getApplicationContext(), ViewRecipes.class));

                return true;
            }
            else if(itemId == R.id.bottom_profile)
            {
                startActivity(new Intent(getApplicationContext(), UserInfo.class));
                return true;
            }
            return false;
        });
    }
}