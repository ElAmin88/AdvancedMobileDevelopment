package com.example.lap.pedometer.ui;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lap.pedometer.R;
import com.example.lap.pedometer.classes.User;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private static User currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        currentUser = null;


        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                if (currentUser == null)
                {
                    Intent mainIntent = new Intent(getBaseContext(),LoginActivity.class);
                    startActivity(mainIntent);
                    finish();
                }

            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    public static void setCurrentUser(User user)
    {
        currentUser = user;
    }

    public static User getCurrentUser()
    {
        return currentUser;
    }
    @Override
    public void onBackPressed() {

    }
}
