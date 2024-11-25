package com.example.cis183_finalproject_kayleebusenbark;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class RegisterNewUser extends AppCompatActivity
{
    DatabaseHelper dbHelper;
    TextInputEditText tiet_j_username;
    TextInputEditText tiet_j_fname;
    TextInputEditText tiet_j_lname;
    TextInputEditText tiet_j_password;
    TextView tv_j_usernameError;
    Button btn_j_register;

    Intent intent_j_login;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_new_user);

        //GUI Connections
        tiet_j_username = findViewById(R.id.tiet_v_register_username);
        tiet_j_fname = findViewById(R.id.tiet_v_register_fname);
        tiet_j_lname = findViewById(R.id.tiet_v_register_lname);
        tiet_j_password = findViewById(R.id.tiet_v_register_password);
        tv_j_usernameError = findViewById(R.id.tv_v_register_usernameError);
        btn_j_register = findViewById(R.id.btn_v_register_register);

        intent_j_login = new Intent(RegisterNewUser.this, MainActivity.class);

        dbHelper = new DatabaseHelper(this);

        tv_j_usernameError.setVisibility(View.INVISIBLE);

        userNameKeyEventListener();
        registerButtonOnClickListener();
    }

    private void userNameKeyEventListener()
    {
        tiet_j_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                TextInputEditText username = tiet_j_username;
                if(!username.getText().toString().isEmpty())
                {
                    checkUsernameExistence(username.getText().toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void checkUsernameExistence(String username)
    {
        boolean exists = dbHelper.usernameExists(username);
        if(exists)
        {
            tv_j_usernameError.setVisibility(View.VISIBLE);
            btn_j_register.setEnabled(false);
        }
        else
        {
            tv_j_usernameError.setVisibility(View.INVISIBLE);
            btn_j_register.setEnabled(true);
        }
    }

    private void addUser()
    {
        if(!tiet_j_username.getText().toString().isEmpty() && !tiet_j_fname.getText().toString().isEmpty() && !tiet_j_lname.toString().isEmpty() && !tiet_j_password.toString().isEmpty())
        {
            String username = tiet_j_username.getText().toString();
            String fname = tiet_j_fname.getText().toString();
            String lname = tiet_j_lname.getText().toString();
            String password = tiet_j_password.getText().toString();

            dbHelper.createNewUser(username, fname, lname, password);
        }
        else
        {
            btn_j_register.setEnabled(false);
        }
    }

    private void registerButtonOnClickListener()
    {
        btn_j_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                addUser();
                startActivity(intent_j_login);
            }
        });
    }





}