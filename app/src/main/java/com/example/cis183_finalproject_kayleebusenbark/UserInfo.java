package com.example.cis183_finalproject_kayleebusenbark;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class UserInfo extends AppCompatActivity
{
    DatabaseHelper dbHelper;
    TextInputEditText tiet_j_username;
    TextInputEditText tiet_j_fname;
    TextInputEditText tiet_j_lname;
    TextInputEditText tiet_j_password;
    Button btn_update;
    Button btn_delete;
    Button btn_loggout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info);

        //GUI Connections
        tiet_j_username = findViewById(R.id.tiet_v_userInfo_username);
        tiet_j_fname = findViewById(R.id.tiet_v_userInfo_fname);
        tiet_j_lname = findViewById(R.id.tiet_v_userInfo_lname);
        tiet_j_password = findViewById(R.id.tiet_v_userInfo_password);
        btn_update = findViewById(R.id.btn_v_userInfo_update);
        btn_delete = findViewById(R.id.btn_v_userInfo_deleteAccount);
        btn_loggout = findViewById(R.id.btn_v_userInfo_loggout);

        dbHelper = new DatabaseHelper(this);

        initUserInfo();
        updateUserInfoEventListener();
    }

    private void initUserInfo()
    {
        User loggedInUser = SessionData.getLoggedInUser();

        if(loggedInUser != null)
        {
            tiet_j_username.setText(loggedInUser.getUsername());
            tiet_j_fname.setText(loggedInUser.getFname());
            tiet_j_lname.setText(loggedInUser.getLname());
            tiet_j_password.setText(loggedInUser.getPassword());
        }
        else
        {
            Log.e("UserInfo", "No logged-in user data found");
        }
    }

    private void updateUserInfoEventListener()
    {
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!tiet_j_username.getText().toString().isEmpty() && !tiet_j_fname.getText().toString().isEmpty() && !tiet_j_lname.getText().toString().isEmpty() && !tiet_j_password.getText().toString().isEmpty())
                {
                    String username = tiet_j_username.getText().toString().trim();
                    String fname = tiet_j_fname.getText().toString().trim();
                    String lname = tiet_j_lname.getText().toString().trim();
                    String password = tiet_j_password.getText().toString().trim();
                    int userId = SessionData.getLoggedInUser().getUserId();

                    User updatedUser = new User(userId, username, fname, lname, password);

                    dbHelper.updateUserInfo(updatedUser);
                    SessionData.setLoggedInUser(updatedUser);

                    Log.e("Update", "Update");
                }
            }
        });
    }

}