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

public class ViewRecipes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_recipes);

        //NAV BAR
        //NAV BAR
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_recipeBook);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if(itemId == R.id.bottom_pantry)
            {
                startActivity(new Intent(getApplicationContext(), Pantry.class));
                return true;
            }
            else if(itemId == R.id.bottom_addRecipe)
            {
                startActivity(new Intent(getApplicationContext(), AddRecipe.class));

                return true;
            }
            else if(itemId == R.id.bottom_recipeBook)
            {
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