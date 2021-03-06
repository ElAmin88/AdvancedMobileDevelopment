package com.example.lap.lecture_3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btn_start, btn_score, btn_login;
    TextView txt_welcome;

    public static String Player ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_start = (Button)findViewById(R.id.btn_startGame);
        btn_score = (Button)findViewById(R.id.btn_highScore);
        btn_login = (Button)findViewById(R.id.btn_login);

        txt_welcome = (TextView)findViewById(R.id.txt_welcome);
        if(Player != "")
        {
            btn_login.setVisibility(View.GONE);
            txt_welcome.setText("Welcome " + Player);

        }
        else
        {
            txt_welcome.setText("Welcome Guest");
        }

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i;
                if(Player == "")
                {
                    Toast.makeText(getBaseContext(),"YOU MUST LOGIN FIRST",Toast.LENGTH_LONG).show();
                    i = new Intent(getBaseContext(),LoginActivity.class);
                }
                else
                    i = new Intent(getBaseContext(),GameActivity.class);
                startActivity(i);
            }
        });

        btn_score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(),ScoreActivity.class);
                startActivity(i);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(),LoginActivity.class);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }
}
