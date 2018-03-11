package com.example.lap.lecture_3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText username;
    Button btn_addPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = (EditText)findViewById(R.id.editText_Username);
        btn_addPlayer =(Button)findViewById(R.id.btn_addplayer);
        btn_addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = username.getText().toString();
                if (name !=""){
                    MainActivity.Player = name;
                    Intent i = new Intent(getBaseContext(),GameActivity.class);
                    startActivity(i);
                }
                else
                    Toast.makeText(getBaseContext(),"YOU MUST ENTER NAME FIRST",Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getBaseContext(),MainActivity.class);
        startActivity(i);
        finish();
    }
}
