package com.example.lap.pedometer.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lap.pedometer.R;
import com.example.lap.pedometer.classes.DatabaseAdapter;
import com.example.lap.pedometer.classes.User;

public class SignupActivity extends AppCompatActivity {

    Button btn_signup;
    TextView link_login;
    DatabaseAdapter databaseAdapter;
    private EditText input_SignupName, input_SignupPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initComponents();
        setClickListeners();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseAdapter.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        databaseAdapter.close();
        finish();
    }

    private void initComponents()
    {
        input_SignupName = (EditText)findViewById(R.id.input_SignupName);
        input_SignupPassword = (EditText)findViewById(R.id.input_SignupPassword);
        btn_signup = (Button)findViewById(R.id.btn_signup);
        link_login = (TextView)findViewById(R.id.link_login);
        databaseAdapter = new DatabaseAdapter(this);
        databaseAdapter.open();

    }

    private void setClickListeners()
    {
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup();
            }
        });

        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }

    private void signup()
    {
        if(!validate())
            return;

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();
        btn_signup.setEnabled(false);
        link_login.setEnabled(false);

        final String name = input_SignupName.getText().toString();
        final String password = input_SignupPassword.getText().toString();

        final User user = new User();
        user.setName(name);
        user.setPassword(password);

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        if(user != null)
                        {
                            long i = databaseAdapter.AddUser(user);
                            if(i > 0)
                            {
                                SplashActivity.setCurrentUser(user);
                                onSignupSuccess();
                            }
                            else
                                onSignupFailed();

                        }
                        else
                        {
                            onSignupFailed();
                        }
                        btn_signup.setEnabled(true);
                        link_login.setEnabled(true);
                        progressDialog.dismiss();
                    }
                }, 3000);
    }

    private boolean validate()
    {
        String name = input_SignupName.getText().toString().replace(" ","");
        String password = input_SignupPassword.getText().toString().replace(" ","");
        if(name.equals(""))
        {
            input_SignupName.setError("Field is empty");
            return false;
        }

        if( name.length()<4 || name.contains(" "))
        {
            input_SignupName.setError("Must be more than 4 characters without space");
            return false;
        }

        User u = databaseAdapter.getUserByName(name);
        if(u != null)
        {
            Toast.makeText(this, "Name Already taken",Toast.LENGTH_LONG).show();
            input_SignupName.setError("Name taken");
            return false;
        }


        if(password.equals("")) {
            input_SignupPassword.setError("Field is empty");
            return false;
        }

        if( password.length()<4 || password.contains(" "))
        {
            input_SignupPassword.setError("Must be more than 4 characters without space characters");
            return false;
        }

        return true;
    }

    private void onSignupSuccess()
    {

        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
        finish();
    }

    private void onSignupFailed()
    {
        Toast.makeText(this, "Wrong Password or Username",Toast.LENGTH_LONG).show();

    }
}
