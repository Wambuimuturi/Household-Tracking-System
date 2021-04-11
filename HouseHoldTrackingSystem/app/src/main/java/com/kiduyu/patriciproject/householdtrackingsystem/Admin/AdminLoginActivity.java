package com.kiduyu.patriciproject.householdtrackingsystem.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.kiduyu.patriciproject.householdtrackingsystem.R;
import com.shashank.sony.fancytoastlib.FancyToast;

public class AdminLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
    }

    public void login(View view) {
        EditText adminUsername = findViewById(R.id.adminUsername);
        EditText adminPassword = findViewById(R.id.adminPassword);

        if (adminUsername.getText().toString().equalsIgnoreCase("admin") && adminPassword.getText().toString().equalsIgnoreCase("admin")){
            startActivity(new Intent(this, AdminHomeActivity.class));
            FancyToast.makeText(this,"You logged in successfully",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,false).show();

        }else {
            FancyToast.makeText(this,"Wrong username or password",FancyToast.LENGTH_SHORT,FancyToast.ERROR,false).show();
        }
    }
}