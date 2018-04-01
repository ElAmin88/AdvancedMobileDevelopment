package com.example.lap.pedometer.ui;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lap.pedometer.R;
import com.example.lap.pedometer.classes.DatabaseAdapter;
import com.example.lap.pedometer.classes.User;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    EditText input_LoginName, input_LoginPassword;
    Button btn_login;
    TextView link_signup;
    DatabaseAdapter databaseAdapter;
    private int failedAttempts; //Number of failed login attempts
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponents();
        setClickListeners();
        ArrayList<User> users = databaseAdapter.getAllUsers();
        users.size();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseAdapter.close();
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        databaseAdapter.close();
        finish();
    }

    private void initComponents()
    {
        input_LoginName = (EditText)findViewById(R.id.input_LoginName);
        input_LoginPassword = (EditText)findViewById(R.id.input_LoginPassword);
        btn_login = (Button)findViewById(R.id.btn_login);
        link_signup = (TextView)findViewById(R.id.link_signup);
        databaseAdapter = new DatabaseAdapter(this);
        databaseAdapter.open();
        failedAttempts = 0;

    }

    private void setClickListeners()
    {
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        link_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void login()
    {
        if (!validate())
        {
            Toast.makeText(this,"You can't leave any field empty!!",Toast.LENGTH_LONG).show();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        btn_login.setEnabled(false);
        link_signup.setEnabled(false);

        final String name = input_LoginName.getText().toString();
        final String password = input_LoginPassword.getText().toString();

        final User user = databaseAdapter.getUserByName(name);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if(user != null && user.getPassword().equals(password))
                        {
                            SplashActivity.setCurrentUser(user);
                            onLoginSuccess();
                        }
                        else
                        {
                            onLoginFailed();
                        }
                        btn_login.setEnabled(true);
                        link_signup.setEnabled(true);
                        progressDialog.dismiss();
                    }
                }, 3000);

    }

    private boolean validate()
    {
        String name = input_LoginName.getText().toString().replace(" ","");
        String password = input_LoginPassword.getText().toString().replace(" ","");
        if(name.equals(""))
        {
            input_LoginName.setError("Field is empty");
            return false;
        }

        if(password.equals("")) {
            input_LoginPassword.setError("Field is empty");
            return false;
        }

        return true;
    }

    private void onLoginSuccess()
    {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);
        finish();
    }

    private void onLoginFailed()
    {
        failedAttempts++;
        Toast.makeText(this, "Wrong Password or Username",Toast.LENGTH_LONG).show();
        if(failedAttempts>3)
        {
            Toast.makeText(this, "You must register before Login",Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, SignupActivity.class);
            startActivity(i);
            finish();
        }
    }


}
