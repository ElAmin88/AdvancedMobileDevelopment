package com.example.lap.lecture_3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ScoreActivity extends AppCompatActivity {
    TextView txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        txt = (TextView)findViewById(R.id.txt_score);
        txt.setText(getIntent().getStringExtra("score"));

    }
}
