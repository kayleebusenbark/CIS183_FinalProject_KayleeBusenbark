package com.example.cis183_finalproject_kayleebusenbark;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class Pantry extends AppCompatActivity implements View.OnClickListener
{
    DatabaseHelper dbHelper;
    LinearLayout layoutList;
    Button btn_addIngredient;
    ArrayAdapter<String> measurementAdapter;

    Dialog searchPopup;
    EditText popupSearch;
    ListView popupListView;
    ArrayAdapter<String> ingredientsAdapter;

    TextView tv_j_addCustomIngredient;
    TextView tv_j_selIngredient;
    //ArrayList<String> ingredientNames;

    Button btn_j_search;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pantry);

        bottomNavBarSetUp();
        //GUI CONNECTIONS
        layoutList = findViewById(R.id.ll_v_pantry);
        btn_addIngredient = findViewById(R.id.btn_pantry_v_addIngredient);
        tv_j_addCustomIngredient = findViewById(R.id.tv_pantry_v_addNewIngredient);
        btn_j_search = findViewById(R.id.btn_v_pantry_searchForRecipes);

        dbHelper = new DatabaseHelper(this);
        btn_addIngredient.setOnClickListener(this);


        ArrayList<String> measurementList = dbHelper.getAllMeasurementsForSpinner();
        measurementList.add(0, "Units:");
        measurementAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, measurementList);

        User user = SessionData.getLoggedInUser();
        int userId = user.getUserId();
        loadPantryData(userId);
        addNewIngredientClickListener();
        searchButtonClickListener();
    }

    private void addView(UserIngredient userIngredient)
    {
        View ingredientView = getLayoutInflater().inflate(R.layout.row_add_pantry, null, false);

        tv_j_selIngredient = ingredientView.findViewById(R.id.tv_pantry_v_addNewIngredient);
        tv_j_selIngredient.setOnClickListener(v -> openIngredientSelectionPopup());
        EditText et_quantity = ingredientView.findViewById(R.id.et_pantry_v_quantity);
        Spinner sp_measurement = ingredientView.findViewById(R.id.sp_pantry_v_measurement);
        ImageView iv_removeIngredient = ingredientView.findViewById(R.id.iv_pantry_v_removeIngredient);
        sp_measurement.setAdapter(measurementAdapter);

        if(userIngredient != null)
        {
            tv_j_selIngredient.setEnabled(false);
            tv_j_selIngredient.setText(dbHelper.getIngredientNameById(userIngredient.getIngredientId()));


            if(userIngredient.getQuantity() == (int) userIngredient.getQuantity())
            {
                String formattedQuantity = String.format("%1.0f", userIngredient.getQuantity());
                et_quantity.setText(formattedQuantity);
            }
            else
            {
                et_quantity.setText(String.valueOf(userIngredient.getQuantity()));
            }

            int measurementIndex = measurementAdapter.getPosition(dbHelper.getMeasurementNamebyId(userIngredient.getMeasurementId()));
            sp_measurement.setSelection(measurementIndex);

            //store the userIngredient object with the view
            ingredientView.setTag(userIngredient);
        }

        iv_removeIngredient.setOnClickListener(v -> {
            layoutList.removeView(ingredientView);

            if(userIngredient != null)
            {
                dbHelper.deleteUserIngredient(SessionData.getLoggedInUser().getUserId(), userIngredient.getUserIngredientId());
            }
            toggleAddButtonState();
        });


        et_quantity.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                updateIngredient(tv_j_selIngredient, et_quantity, sp_measurement, ingredientView);
                toggleAddButtonState();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        sp_measurement.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                updateIngredient(tv_j_selIngredient, et_quantity, sp_measurement, ingredientView);
                toggleAddButtonState();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        layoutList.addView(ingredientView);
        toggleAddButtonState();

    }

    private void disableLastViewElement()
    {
        if(layoutList.getChildCount() > 0)
        {
            View lastAddedView = layoutList.getChildAt(layoutList.getChildCount() -1);

            TextView tv_ingredientSel = lastAddedView.findViewById(R.id.tv_pantry_v_addNewIngredient);
            tv_ingredientSel.setEnabled(false);
        }
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

        for(int i = 0; i < layoutList.getChildCount(); i++)
        {
            View childView = layoutList.getChildAt(i);
            TextView tv_ingredientSel = childView.findViewById(R.id.tv_pantry_v_addNewIngredient);
            EditText et_quantity = childView.findViewById(R.id.et_pantry_v_quantity);
            Spinner sp_measurement = childView.findViewById(R.id.sp_pantry_v_measurement);

            boolean isValid = !et_quantity.getText().toString().isEmpty() && !et_quantity.getText().toString().equals("0") && !tv_ingredientSel.getText().toString().isEmpty() && sp_measurement.getSelectedItemPosition() !=0;

            isAllValid &= isValid;
        }
        btn_addIngredient.setEnabled(isAllValid);
    }

    private void updateIngredient(TextView tv_ingredientSel, EditText et_quantity, Spinner sp_measurement, View ingredientView)
    {
        String ingredientName = tv_ingredientSel.getText().toString();
        String quantityText = et_quantity.getText().toString();
        int measurementPosition = sp_measurement.getSelectedItemPosition();

        if(!ingredientName.isEmpty() && !quantityText.isEmpty() && !quantityText.equals("0") && measurementPosition !=0)
        {
            try
            {
                float quantity = Float.parseFloat(quantityText);
                int ingredientId = dbHelper.getIngredientIdByName(ingredientName);
                int measurementId = dbHelper.getMeasurementIdByName(sp_measurement.getSelectedItem().toString());

                UserIngredient existingIngredient = (UserIngredient) ingredientView.getTag();

                if(existingIngredient != null)
                {
                    dbHelper.updateUserIngredientList(SessionData.getLoggedInUser().getUserId(), existingIngredient.getUserIngredientId(), quantity, measurementId);
                }
                else
                {
                    dbHelper.addUserIngredients(SessionData.getLoggedInUser().getUserId(), ingredientId, quantity, measurementId);
                    UserIngredient newIngredient = new UserIngredient(SessionData.getLoggedInUser().getUserId(), ingredientId, quantity, measurementId);
                    ingredientView.setTag(newIngredient);
                }
            }
            catch (NumberFormatException e)
            {
                Toast.makeText(this, "Invalid quantity. Please enter a valid number", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openIngredientSelectionPopup()
    {

        searchPopup = new Dialog(this);
        searchPopup.setContentView(R.layout.custom_spinner_ingredients);
        searchPopup.getWindow().setLayout(700, 1000);
        searchPopup.show();

        popupSearch = searchPopup.findViewById(R.id.et_search);
        popupListView = searchPopup.findViewById(R.id.lv_ingredients);

        ArrayList<String> ingredientNames = dbHelper.getAllIngredientNamesForSpinner();
        for(int i = 0; i <layoutList.getChildCount(); i++)
        {
            View childView = layoutList.getChildAt(i);
            TextView tv_ingredientSel = childView.findViewById(R.id.tv_pantry_v_addNewIngredient);
            String selectIngredient = tv_ingredientSel.getText().toString();
            ingredientNames.remove(selectIngredient);
        }
        ingredientsAdapter = new ArrayAdapter<>(Pantry.this, android.R.layout.simple_list_item_1, ingredientNames );
        popupListView.setAdapter(ingredientsAdapter);

        popupSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ingredientsAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        foodSelPopUpClickListener();

    }

    private void foodSelPopUpClickListener()
    {
        popupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tv_j_selIngredient.setText(ingredientsAdapter.getItem(i));
                searchPopup.dismiss();
            }
        });
    }



    @Override
    public void onClick(View view)
    {
        disableLastViewElement();
        addView(null);
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
        ArrayAdapter<String> ingredientCategoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, categoryList);
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

    private void searchButtonClickListener()
    {
        btn_j_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Pantry.this, SearchResults.class));
            }
        });
    }



    private void bottomNavBarSetUp()
    {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.bottom_pantry);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.bottom_pantry) {
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


