package com.example.cis183_finalproject_kayleebusenbark;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    DatabaseHelper dbHelper;
    TextInputEditText tiet_j_username;
    TextInputEditText tiet_j_password;
    Button btn_j_login;
    TextView tv_j_register;
    TextView tv_j_error;

    Intent intent_j_pantry;
    //Intent intent_j_userInfo;
    Intent intent_j_register;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //GUI connections
        tiet_j_username = findViewById(R.id.tiet_v_login_username);
        tiet_j_password = findViewById(R.id.tiet_v_userInfo_password);
        btn_j_login = findViewById(R.id.btn_v_login_login);
        tv_j_register = findViewById(R.id.tv_v_login_register);
        tv_j_error = findViewById(R.id.tv_v_register_usernameError);
        //intent_j_userInfo = new Intent(MainActivity.this, UserInfo.class);
        intent_j_pantry = new Intent(MainActivity.this, Pantry.class);
        intent_j_register = new Intent(MainActivity.this, RegisterNewUser.class);


        dbHelper = new DatabaseHelper(this);
        dbHelper.initAllTables();

        tv_j_error.setVisibility(View.INVISIBLE);
        //testing purposes
        checkALlTableCounts();
        loginButtonClickListener();
        registerOnClickListener();


        //testing database with hard coded
    }

    //Testing purposes
    private void checkALlTableCounts()
    {
        Log.d("USERS COUNT: ", dbHelper.countRecordsFromTable(dbHelper.getUserDbName()) + "");
        Log.d("INGREDIENTS COUNT: ", dbHelper.countRecordsFromTable(dbHelper.getIngredientsDbName()) + "");
        Log.d("MEASUREMENT TYPE COUNT: ", dbHelper.countRecordsFromTable(dbHelper.getMeasurementDbName()) + "");
        Log.d("RECIPE COUNT: ", dbHelper.countRecordsFromTable(dbHelper.getRecipeDbName()) + "");
        Log.d("CUISINE CATEGORY: ", dbHelper.countRecordsFromTable(dbHelper.getCuisineDbName()) + "");
        Log.d("INGREDIENT CATEGORY: ", dbHelper.countRecordsFromTable(dbHelper.getIngredientCategoryDbName()) + "");
        Log.d("RECIPE INGREDIENTS: ", dbHelper.countRecordsFromTable(dbHelper.getRecipeIngredientsDbName()) + "");
        Log.d("USER FAVORITES: ", dbHelper.countRecordsFromTable(dbHelper.getUserFavoritesDbName()) + "");
        Log.d("USER INGREDIENTS: ", dbHelper.countRecordsFromTable(dbHelper.getUserIngredientsDbName()) + "");
    }

    private void loginButtonClickListener()
    {
        btn_j_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String username = tiet_j_username.getText().toString().trim();
                String password = tiet_j_password.getText().toString().trim();

                if(dbHelper.isLoginValid(username, password))
                {
                    dbHelper.getAllUserDataGivenUsername(username);
                    Log.e("Login", "Success");
                    tv_j_error.setVisibility(View.INVISIBLE);
                    startActivity(intent_j_pantry);

                }
                else
                {
                    tv_j_error.setVisibility(View.VISIBLE);
                    Log.e("Login", "Invalid username or password");
                }

            }
        });
    }

    private void registerOnClickListener()
    {
        tv_j_register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                startActivity(intent_j_register);
            }
        });
    }

}