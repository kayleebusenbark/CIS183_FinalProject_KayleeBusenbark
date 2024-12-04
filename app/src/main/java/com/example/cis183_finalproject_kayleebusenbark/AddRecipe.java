package com.example.cis183_finalproject_kayleebusenbark;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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

public class AddRecipe extends AppCompatActivity implements View.OnClickListener {
    DatabaseHelper dbHelper;
    LinearLayout layoutList;
    Button btn_j_addIngredient;
    Button btn_j_addToRecipeBook;
    ArrayAdapter<String> measurementAdapter;
    Dialog searchPopup;
    EditText popupSearch;
    ListView popupListView;
    ArrayAdapter<String> ingredientsAdapter;

    EditText titleEditText;
    EditText instructionEditText;
    EditText preptimeEditText;
    Spinner recipeCategorySpinner;
    Spinner prepTimeCategorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_recipe);

        dbHelper = new DatabaseHelper(this);
        titleEditText = findViewById(R.id.et_v_addRecipe_recipeTitle);
        instructionEditText = findViewById(R.id.et_v_addRecipe_Instructions);
        preptimeEditText = findViewById(R.id.et_v_addRecipe_prepTime);

        layoutList = findViewById(R.id.ll_v_addRecipe);
        btn_j_addIngredient = findViewById(R.id.btn_v_addRecipe_addAnotherIngredient);
        btn_j_addToRecipeBook = findViewById(R.id.btn_v_addRecipe_addToRecipeBook);

        //Measurement Adapter
        ArrayList<String> measurementList = dbHelper.getAllMeasurementsForSpinner();
        measurementList.add(0, "Units");
        measurementAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, measurementList);

        //reciepeCategoryAdapter
        recipeCategorySpinner = findViewById(R.id.sp_v_addRecipe_recipeCategory);
        ArrayList<String> recipeCategoryList = dbHelper.getAllRecipeCategoriesForSpinner();
        recipeCategoryList.add(0, "Select Category:");
        ArrayAdapter<String> reciepeCategoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, recipeCategoryList);
        recipeCategorySpinner.setAdapter(reciepeCategoryAdapter);

        //prep time category adapter
        prepTimeCategorySpinner = findViewById(R.id.sp_v_addRecipe_prepTimeCategory);
        ArrayList<String> prepTimeCategoryList = Recipe.prepTimeCategory.getAllRanges();
        ArrayAdapter<String> prepTimeCategoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, prepTimeCategoryList);
        prepTimeCategorySpinner.setAdapter(prepTimeCategoryAdapter);

        validationCheckListeners();
        //set click listeners
        btn_j_addIngredient.setOnClickListener(this);
        btn_j_addToRecipeBook.setOnClickListener(view -> saveRecipe());
        toggleAddButtonState();
        bottomNavBarSetUp();
    }

    @Override
    public void onClick(View view)
    {
        if(view.getId() == R.id.btn_v_addRecipe_addAnotherIngredient)
        {
            addIngredientRow();
        }
    }

    private void validationCheckListeners()
    {
        titleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                toggleAddButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        instructionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                toggleAddButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        preptimeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                toggleAddButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        prepTimeCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toggleAddButtonState();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        recipeCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toggleAddButtonState();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void addIngredientRow()
    {
        if(!isLastRowValid())
        {
            Toast.makeText(this, "Please fill out all fields before addding a new ingredient", Toast.LENGTH_SHORT).show();
            return;
        }

        View ingredientView = getLayoutInflater().inflate(R.layout.row_add_pantry, null, false);

        //GUI CONNECTIONS

        TextView tv_j_selIngredient = ingredientView.findViewById(R.id.tv_pantry_v_addNewIngredient);
        EditText et_j_quantity = ingredientView.findViewById(R.id.et_pantry_v_quantity);
        Spinner sp_j_measurement = ingredientView.findViewById(R.id.sp_pantry_v_measurement);
        ImageView iv_j_removeIngredient = ingredientView.findViewById(R.id.iv_pantry_v_removeIngredient);

        sp_j_measurement.setAdapter(measurementAdapter);

        tv_j_selIngredient.setOnClickListener(view -> openIngredientSelectionPopup(tv_j_selIngredient));

        iv_j_removeIngredient.setOnClickListener(view -> {
            layoutList.removeView(ingredientView);
            toggleAddButtonState();
        });

        et_j_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                toggleAddButtonState();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sp_j_measurement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                toggleAddButtonState();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        layoutList.addView(ingredientView);
        toggleAddButtonState();

    }
    private void openIngredientSelectionPopup(TextView selectedIngredientView)
    {
        searchPopup = new Dialog(this);
        searchPopup.setContentView(R.layout.custom_spinner_ingredients);
        searchPopup.getWindow().setLayout(700, 1000);
        searchPopup.show();

        popupSearch = searchPopup.findViewById(R.id.et_search);
        popupListView = searchPopup.findViewById(R.id.lv_ingredients);

        //load ingredients from database

        ArrayList<String> ingredientNames = dbHelper.getAllIngredientNamesForSpinner();
        for(int i = 0; i< layoutList.getChildCount(); i++)
        {
            View childView = layoutList.getChildAt(i);
            TextView tv_ingredientSel = childView.findViewById(R.id.tv_pantry_v_addNewIngredient);
            ingredientNames.remove(tv_ingredientSel.getText().toString());
        }
        ingredientsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ingredientNames);
        popupListView.setAdapter(ingredientsAdapter);

        //filter ingredients based on search input
        popupSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                ingredientsAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        popupListView.setOnItemClickListener((parent, view, position, id) -> {
            selectedIngredientView.setText(ingredientsAdapter.getItem(position));
            searchPopup.dismiss();
        });
    }

    private boolean isLastRowValid()
    {
        if(layoutList.getChildCount() == 0)
        {
            return true;
        }

        //get the last row in the layout

        View lastRow = layoutList.getChildAt(layoutList.getChildCount() - 1);
        TextView tv_selIngredient = lastRow.findViewById(R.id.tv_pantry_v_addNewIngredient);
        EditText et_quantity = lastRow.findViewById(R.id.et_pantry_v_quantity);
        Spinner sp_measurement = lastRow.findViewById(R.id.sp_pantry_v_measurement);

        boolean isIngredientSelected = !tv_selIngredient.getText().toString().isEmpty();
        boolean isQuantityValid = !et_quantity.getText().toString().isEmpty() && !et_quantity.getText().toString().equals("0");
        boolean isMeasurementSelected = sp_measurement.getSelectedItemPosition() != 0;

        return isIngredientSelected && isQuantityValid && isMeasurementSelected;
    }

    private boolean isFormValid()
    {
        String title = titleEditText.getText().toString();
        if(title.isEmpty())
        {
            return false;
        }

        String instructions = instructionEditText.getText().toString();
        if(instructions.isEmpty())
        {
            return false;
        }

        String prepTimeText = preptimeEditText.getText().toString();
        if(prepTimeText.isEmpty() || Float.parseFloat(prepTimeText) <= 0)
        {
            return false;
        }

        if(prepTimeCategorySpinner.getSelectedItemPosition() == 0)
        {
            return false;
        }

        if(recipeCategorySpinner.getSelectedItemPosition() == 0)
        {
            return false;
        }
        for(int i = 0; i < layoutList.getChildCount(); i++)
        {
            View childView = layoutList.getChildAt(i);

            TextView tv_selIngredient = childView.findViewById(R.id.tv_pantry_v_addNewIngredient);
            EditText et_quantity = childView.findViewById(R.id.et_pantry_v_quantity);
            Spinner sp_measurement = childView.findViewById(R.id.sp_pantry_v_measurement);

            boolean isIngredientSelected = !tv_selIngredient.getText().toString().isEmpty();
            boolean isQuantityValid = !et_quantity.getText().toString().isEmpty() && !et_quantity.getText().toString().equals("0");
            boolean isMeasurementSelected = sp_measurement.getSelectedItemPosition() != 0;

            if(!isIngredientSelected || !isQuantityValid || !isMeasurementSelected)
            {
                return false;
            }

        }

        return true;
    }


    private void toggleAddButtonState()
    {
        btn_j_addIngredient.setEnabled(isLastRowValid());
        btn_j_addToRecipeBook.setEnabled(isFormValid());
    }

    private void saveRecipe()
    {
        User user = SessionData.getLoggedInUser();

        String title = ((EditText) findViewById(R.id.et_v_addRecipe_recipeTitle)).getText().toString();
        String instructions = ((EditText) findViewById(R.id.et_v_addRecipe_Instructions)).getText().toString();

        //validate inputs
        if(title.isEmpty() || instructions.isEmpty() || layoutList.getChildCount() == 0)
        {
            Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        try
        {
            int userId = user.getUserId();
            float prepTime = Float.parseFloat(((EditText) findViewById(R.id.et_v_addRecipe_prepTime)).getText().toString());
            String prepTimeCategory = ((Spinner) findViewById(R.id.sp_v_addRecipe_prepTimeCategory)).getSelectedItem().toString();
            int recipeCategoryId = dbHelper.getRecipeCategoryIdByName(((Spinner) findViewById(R.id.sp_v_addRecipe_recipeCategory)).getSelectedItem().toString());

            int recipeId = dbHelper.addRecipe(userId, title, instructions, prepTime, prepTimeCategory, recipeCategoryId);

            //save ingredient for the recipe

            for(int i = 0; i <layoutList.getChildCount(); i++)
            {
                View childView = layoutList.getChildAt(i);
                TextView tv_selIngredient = childView.findViewById(R.id.tv_pantry_v_addNewIngredient);
                EditText et_quantity = childView.findViewById(R.id.et_pantry_v_quantity);
                Spinner sp_measurement = childView.findViewById(R.id.sp_pantry_v_measurement);

                if(!tv_selIngredient.getText().toString().isEmpty() && !et_quantity.getText().toString().isEmpty() && sp_measurement.getSelectedItemPosition() != 0)
                {
                    int ingredientId = dbHelper.getIngredientIdByName(tv_selIngredient.getText().toString());
                    float quantity = Float.parseFloat(et_quantity.getText().toString());
                    int measurementId = dbHelper.getMeasurementIdByName(sp_measurement.getSelectedItem().toString());

                    dbHelper.addRecipeIngredient(recipeId, ingredientId, quantity, measurementId);
                }
            }
            Toast.makeText(this, "Recipe save succesfully!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddRecipe.this, ViewRecipes.class));
        }
        catch (NumberFormatException e)
        {
            Toast.makeText(this, "Invalid preparation time. Please enter a valid number", Toast.LENGTH_SHORT).show();

        }
    }


    private void bottomNavBarSetUp()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_addRecipe);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_pantry) {
                startActivity(new Intent(getApplicationContext(), Pantry.class));
                return true;
            } else if (itemId == R.id.bottom_addRecipe) {
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