package com.example.cis183_finalproject_kayleebusenbark;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class RecipeDetails extends AppCompatActivity {

    DatabaseHelper dbhelper;

    EditText et_j_recipeTitle;
    EditText et_j_recipeInstructions;
    Spinner sp_j_recipeCategory;
    EditText et_j_prepTime;
    Spinner sp_j_prepTimeCategory;
    Button btn_j_editRecipe;

    LinearLayout ll_j_ingredientList;
    ArrayAdapter<String> measurementAdapter;
    ArrayAdapter<String> reciepeCategoryAdapter;

    TextView tv_j_addToFavorites;

    Recipe selectedRecipe = SessionDataForRecipes.getSelectedRecipe();
    User loggedInUser = SessionData.getLoggedInUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recipe_details);

        bottomNavBarSetUp();

        //GUI CONNECTIONS
        et_j_recipeTitle = findViewById(R.id.et_v_recipeDetails_recipeTitle);
        et_j_recipeInstructions = findViewById(R.id.et_v_recipeDetails_Instructions);
        et_j_recipeInstructions.setMovementMethod(new ScrollingMovementMethod());
        sp_j_recipeCategory = findViewById(R.id.sp_v_recipeDetails_recipeCategory);
        et_j_prepTime = findViewById(R.id.et_v_recipeDetails_prepTime);
        sp_j_prepTimeCategory = findViewById(R.id.sp_v_recipeDetails_prepTimeCategory);
        btn_j_editRecipe = findViewById(R.id.btn_v_recipeDetails_edit);
        ll_j_ingredientList = findViewById(R.id.ll_v_recipeDetails);
        tv_j_addToFavorites = findViewById(R.id.tv_v_recipeDetails_addToFavorites);


        dbhelper = new DatabaseHelper(this);

        //reciepeCategoryAdapter
        ArrayList<String> recipeCategoryList = dbhelper.getAllRecipeCategoriesForSpinner();
        recipeCategoryList.add(0, "Select Category:");
        reciepeCategoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, recipeCategoryList);
        sp_j_recipeCategory.setAdapter(reciepeCategoryAdapter);

        //prep time category adapter
        ArrayList<String> prepTimeCategoryList = Recipe.prepTimeCategory.getAllRanges();
        ArrayAdapter<String> prepTimeCategoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, prepTimeCategoryList);
        sp_j_prepTimeCategory.setAdapter(prepTimeCategoryAdapter);

        checkUserAuthorization();
        selectedRecipe = SessionDataForRecipes.getSelectedRecipe();
        populateRecipeDetails(selectedRecipe);

        setUpDynamicEditTextResizing();
        addToFavoritesOnClickListener();
    }

    private void setUpDynamicEditTextResizing()
    {
        et_j_recipeInstructions.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void afterTextChanged(Editable editable)
            {
                adjustEditTextHeight(et_j_recipeInstructions);
            }
        });
    }

    private void adjustEditTextHeight(EditText editText)
    {
        int lineCount = editText.getLineCount();
        int newHeight = lineCount * editText.getLineHeight();

        int minHeight = (int) (100 * getResources().getDisplayMetrics().density);
        int maxHeight = (int) (500 * getResources().getDisplayMetrics().density);

        newHeight = Math.max(minHeight, Math.min(newHeight, maxHeight));

        editText.setHeight(newHeight);
    }




    private void checkUserAuthorization()
    {
        if(loggedInUser.getUserId() != selectedRecipe.getUserId())
        {
            et_j_recipeTitle.setClickable(false);
            et_j_recipeTitle.setFocusable(false);

            et_j_recipeInstructions.setClickable(false);
            et_j_recipeInstructions.setFocusable(false);


            sp_j_recipeCategory.setClickable(false);
            sp_j_recipeCategory.setFocusable(false);
            sp_j_recipeCategory.setOnTouchListener((v, event) -> true);


            et_j_prepTime.setClickable(false);
            et_j_prepTime.setFocusable(false);

            sp_j_prepTimeCategory.setClickable(false);
            sp_j_prepTimeCategory.setFocusable(false);
            sp_j_prepTimeCategory.setOnTouchListener((v, event) -> true);



            btn_j_editRecipe.setVisibility(View.GONE);
        }
    }
    private void populateRecipeDetails(Recipe recipe)
    {
        et_j_recipeTitle.setText(recipe.getRecipeTitle());
        et_j_recipeInstructions.setText(recipe.getRecipeInstructions());
        adjustEditTextHeight(et_j_recipeInstructions);
        sp_j_recipeCategory.setSelection(reciepeCategoryAdapter.getPosition(dbhelper.getRecipeCategoryNameById(selectedRecipe.getRecipeCatId())));

        et_j_prepTime.setText(String.valueOf(recipe.getRecipePrepTime()));

        //setting recipe prep time to get rid of trailing 0s
        if(recipe.getRecipePrepTime() == (int) recipe.getRecipePrepTime())
        {
            String formattedQuantity = String.format("%1.0f", recipe.getRecipePrepTime());
            et_j_prepTime.setText(formattedQuantity);
        }
        else
        {
            et_j_prepTime.setText(String.valueOf(recipe.getRecipePrepTime()));
        }

        String prepTimeCategory = dbhelper.getPrepTimeCategoryForRecipe(recipe.getRecipeId());
        sp_j_prepTimeCategory.setSelection(((ArrayAdapter<String>) sp_j_prepTimeCategory.getAdapter()).getPosition(prepTimeCategory));

        loadRecipeIngredients();

    }


    private void addView(RecipeIngredient recipeIngredient)
    {
        View ingredientView = getLayoutInflater().inflate(R.layout.row_add_pantry, null, false);

        TextView tv_j_selIngredient = ingredientView.findViewById(R.id.tv_pantry_v_addNewIngredient);
        EditText et_j_quantity = ingredientView.findViewById(R.id.et_pantry_v_quantity);
        Spinner sp_j_measurement = ingredientView.findViewById(R.id.sp_pantry_v_measurement);

        //we only want to see the removeingredient button if the recipe was created by them because they can only edit recipes they created
        ImageView iv_removeIngredient = ingredientView.findViewById(R.id.iv_pantry_v_removeIngredient);

        tv_j_selIngredient.setText(dbhelper.getIngredientNameById(recipeIngredient.getIngredientId()));

        //setting recipe ingredient quantity to get rid of trailing 0s
        if(recipeIngredient.getIngredientQuantity() == (int) recipeIngredient.getIngredientQuantity())
        {
            String formattedQuantity = String.format("%1.0f", recipeIngredient.getIngredientQuantity());
            et_j_quantity.setText(formattedQuantity);
        }
        else
        {
            et_j_quantity.setText(String.valueOf(recipeIngredient.getIngredientQuantity()));
        }

        //measurementAdapter
        ArrayList<String> measurementList = dbhelper.getAllMeasurementsForSpinner();
        measurementList.add(0, "Units:");
        measurementAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, measurementList);
        sp_j_measurement.setAdapter(measurementAdapter);
        sp_j_measurement.setSelection(measurementAdapter.getPosition(dbhelper.getMeasurementNamebyId(recipeIngredient.getMeasurementId())));


        ll_j_ingredientList.addView(ingredientView);
    }

    private void loadRecipeIngredients()
    {

        int recipeId = selectedRecipe.getRecipeId();

        ArrayList<RecipeIngredient> recipeIngredients = dbhelper.getIngredientsByRecipeId(recipeId);

        ll_j_ingredientList.removeAllViews();

        for (RecipeIngredient recipeIngredient : recipeIngredients)
        {
            addView(recipeIngredient);
        }
        checkIngredientEditingAuthorization();
    }

    private void checkIngredientEditingAuthorization()
    {
        if(loggedInUser.getUserId() != selectedRecipe.getUserId())
        {
            for(int i= 0; i < ll_j_ingredientList.getChildCount(); i++)
            {
                View ingredientView = ll_j_ingredientList.getChildAt(i);

                TextView tv_j_ingredientSel = ingredientView.findViewById(R.id.tv_pantry_v_addNewIngredient);
                EditText et_j_quantity = ingredientView.findViewById(R.id.et_pantry_v_quantity);
                Spinner sp_j_measurement = ingredientView.findViewById(R.id.sp_pantry_v_measurement);
                ImageView iv_removeIngredient = ingredientView.findViewById(R.id.iv_pantry_v_removeIngredient);

                tv_j_ingredientSel.setClickable(false);
                tv_j_ingredientSel.setFocusable(false);
                tv_j_ingredientSel.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
                et_j_quantity.setClickable(false);
                et_j_quantity.setFocusable(false);
                sp_j_measurement.setClickable(false);
                sp_j_measurement.setFocusable(false);
                sp_j_measurement.setOnTouchListener((v, event) -> true);

                iv_removeIngredient.setVisibility(View.GONE);

            }
        }
    }

    private void addToFavoritesOnClickListener()
    {
        tv_j_addToFavorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                int userId = loggedInUser.getUserId();
                int recipeId = selectedRecipe.getRecipeId();

                dbhelper.addRecipeToFavorites(userId, recipeId);

                Toast.makeText(RecipeDetails.this, "RECIPE ADDED TO FAVORITES!", Toast.LENGTH_SHORT).show();

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