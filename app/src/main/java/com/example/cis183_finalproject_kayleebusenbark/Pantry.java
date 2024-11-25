package com.example.cis183_finalproject_kayleebusenbark;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class Pantry extends AppCompatActivity implements View.OnClickListener
{
    DatabaseHelper dbHelper;
    LinearLayout layoutList;
    Button btn_addIngredient;
    Spinner sp_j_ingredients;
    ArrayAdapter<String> ingredientsAdapter;
    ArrayAdapter<String> measurementAdapter;
    ArrayAdapter<String> ingredientCategoryAdapter;


    EditText et_j_quantity;
    Spinner sp_j_measurement;
    Button btn_j_removeIngredient;
    UserIngredient userIngredient;
    TextView tv_j_addCustomIngredient;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantry);

        //GUI CONNECTIONS
        layoutList = findViewById(R.id.ll_v_pantry);
        btn_addIngredient = findViewById(R.id.btn_pantry_v_addIngredient);
        tv_j_addCustomIngredient = findViewById(R.id.tv_pantry_v_addNewIngredient);


        dbHelper = new DatabaseHelper(this);

        btn_addIngredient.setOnClickListener(this);

        //spinner for ingredients
        ArrayList<String> ingredientNameList = dbHelper.getAllIngredientNamesForSpinner();
        ingredientNameList.add(0, "Ingredients:");
        ingredientsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, ingredientNameList);

        //spinner for measurements
        ArrayList<String> measurementList = dbHelper.getAllMeasurementsForSpinner();
        measurementList.add(0, "Units:");
        measurementAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, measurementList);


        loadPantryData(SessionData.getLoggedInUser().getUserId());

        addNewIngredientClickListener();
    }

    private void addNewIngredientClickListener()
    {
        tv_j_addCustomIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addIngredientDialog();
            }
        });
    }

    private void addIngredientDialog()
    {
        Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.dialogbox_add_new_ingredient);
        dialog.setCancelable(true);

        TextInputEditText tiet_j_ingredientName = dialog.findViewById(R.id.tiet_dialog_addNewIngredient);
        Spinner sp_j_ingredientCategory = dialog.findViewById(R.id.sp_dialog_addNewIngredient);
        Button btn_j_add = dialog.findViewById(R.id.btn_dialog_add);
        Button btn_j_back = dialog.findViewById(R.id.btn_dialog_back);

        //spinner for categories
        ArrayList<String> categoryList = dbHelper.getAllIngredientCategoriesForSpinner();
        categoryList.add(0, "Select Category:");
        ingredientCategoryAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, categoryList);
        sp_j_ingredientCategory.setAdapter(ingredientCategoryAdapter);

        btn_j_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String ingredientName = tiet_j_ingredientName.getText().toString().trim();

                if(ingredientName.isEmpty())
                {
                    Toast.makeText(Pantry.this, "Ingredient name cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                ArrayList<String> existingIngredientNames = dbHelper.getAllIngredientNamesForSpinner();
                if(existingIngredientNames.contains(ingredientName))
                {
                    Toast.makeText(Pantry.this, "Ingredient name already exists", Toast.LENGTH_LONG).show();
                    return;
                }

                int selectedCategoryPosition = sp_j_ingredientCategory.getSelectedItemPosition();
                if(selectedCategoryPosition == 0)
                {
                    Toast.makeText(Pantry.this, "Please select a category", Toast.LENGTH_LONG).show();
                    return;
                }
                String selectedCategory = sp_j_ingredientCategory.getSelectedItem().toString();
                int categoryId = dbHelper.getIngredientCategoryIdByName(selectedCategory);

                if(!ingredientName.isEmpty() && selectedCategoryPosition != 0)
                {
                    dbHelper.addIngredient(ingredientName, categoryId);
                    ingredientsAdapter.add(ingredientName);
                    ingredientsAdapter.notifyDataSetChanged();
                    dialog.dismiss();

                    Toast.makeText(Pantry.this, "Ingredient added", Toast.LENGTH_LONG).show();
                }
            }
        });


        btn_j_back.setOnClickListener(view -> dialog.dismiss());

        dialog.show();

    }



    private void addView(UserIngredient userIngredient)
    {
        this.userIngredient = userIngredient;

        View ingredientView = getLayoutInflater().inflate(R.layout.row_add_pantry, null, false);

        sp_j_ingredients = ingredientView.findViewById(R.id.sp_dialog_addNewIngredient);
        et_j_quantity = ingredientView.findViewById(R.id.et_pantry_v_quantity);
        sp_j_measurement = ingredientView.findViewById(R.id.sp_pantry_v_measurement);
        btn_j_removeIngredient = ingredientView.findViewById(R.id.btn_pantry_v_removeIngredient);
        sp_j_ingredients.setAdapter(ingredientsAdapter);
        sp_j_measurement.setAdapter(measurementAdapter);

        for(int i = 0; i < layoutList.getChildCount(); i++)
        {
            View childView = layoutList.getChildAt(i);
            Spinner existingSpinner = childView.findViewById(R.id.sp_dialog_addNewIngredient);
            existingSpinner.setEnabled(false);
        }
        sp_j_ingredients.setEnabled(true);

        if(userIngredient != null )
        {
            if(userIngredient.getQuantity() == (int) userIngredient.getQuantity())
            {
                String formattedQuantity = String.format("%1$.0f", userIngredient.getQuantity());
                et_j_quantity.setText(formattedQuantity);
            }
            else
            {
                et_j_quantity.setText(String.valueOf(userIngredient.getQuantity()));
            }

            int ingredientIndex = ingredientsAdapter.getPosition(dbHelper.getIngredientNameById(userIngredient.getIngredientId()));
            sp_j_ingredients.setSelection(ingredientIndex);

            sp_j_ingredients.setEnabled(false);

            // Set selected item in measurement spinner
            int measurementIndex = measurementAdapter.getPosition(dbHelper.getMeasurementNamebyId(userIngredient.getMeasurementId()));
            sp_j_measurement.setSelection(measurementIndex);
        }

        et_j_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                updateOrAddIngredient();
                toggleAddButtonState();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sp_j_ingredients.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                updateOrAddIngredient();
                toggleAddButtonState();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_j_measurement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                updateOrAddIngredient();
                toggleAddButtonState();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        btn_j_removeIngredient.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                View parentView = (View) view.getParent();

                removeView(parentView);
                if(userIngredient != null)
                {
                    dbHelper.deleteUserIngredient(SessionData.getLoggedInUser().getUserId(), userIngredient.getUserIngredientId());
                }
                toggleAddButtonState();
            }
        });

        layoutList.addView(ingredientView);
    }

    private void removeView(View view)
    {
        layoutList.removeView(view);
    }

    @Override
    public void onClick(View view)
    {
        addView(null);
    }

    private void loadPantryData(int userId)
    {
        ArrayList<UserIngredient> userIngredients = dbHelper.getUserIngredients(userId);

        for(UserIngredient userIngredient : userIngredients)
        {
            addView(userIngredient);
        }
    }

    private void toggleAddButtonState()
    {
        boolean isAllValid = true;
        for (int i= 0; i< layoutList.getChildCount(); i++)
        {
            View childView = layoutList.getChildAt(i);
            sp_j_ingredients = childView.findViewById(R.id.sp_dialog_addNewIngredient);
            et_j_quantity = childView.findViewById(R.id.et_pantry_v_quantity);
            sp_j_measurement = childView.findViewById(R.id.sp_pantry_v_measurement);

            boolean isValid = !et_j_quantity.getText().toString().isEmpty() &&!et_j_quantity.getText().toString().equals("0") && sp_j_ingredients.getSelectedItemPosition() != 0 && sp_j_measurement.getSelectedItemPosition() !=0;

            isAllValid &= isValid;
        }

        btn_addIngredient.setEnabled(isAllValid);
    }

    private void updateOrAddIngredient()
    {
        int ingredientPosition = sp_j_ingredients.getSelectedItemPosition();
        String quantityText = et_j_quantity.getText().toString();
        int measurementPosition = sp_j_measurement.getSelectedItemPosition();

        if(ingredientPosition != 0 && !quantityText.isEmpty() && !quantityText.equals("0") && measurementPosition != 0)
        {
            try
            {
                float quantity = Float.parseFloat(quantityText);

                int ingredientId = dbHelper.getIngredientIdByName(sp_j_ingredients.getSelectedItem().toString());
                int measurementId = dbHelper.getMeasurementIdByName(sp_j_measurement.getSelectedItem().toString());

                if(userIngredient != null )
                {
                    dbHelper.updateUserIngredientList(SessionData.getLoggedInUser().getUserId(), userIngredient.getUserIngredientId(), quantity, measurementId);
                }
                else
                {
                    dbHelper.addUserIngredients(SessionData.getLoggedInUser().getUserId(), ingredientId, quantity, measurementId);
                }
            }
            catch (NumberFormatException e)
            {
                Toast.makeText(this, "Invalid quantity. Please enter a valid number", Toast.LENGTH_SHORT).show();
            }
        }
    }
}