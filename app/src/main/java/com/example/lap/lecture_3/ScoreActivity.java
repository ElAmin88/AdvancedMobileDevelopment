package com.example.lap.lecture_3;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Set;

public class ScoreActivity extends AppCompatActivity {

    ListView lstView;
    Set<String> scores;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        lstView =(ListView)findViewById(R.id.lstView);
        GameActivity.loginPreferences = getSharedPreferences("Scores", MODE_PRIVATE);
        GameActivity.loginPrefsEditor = GameActivity.loginPreferences.edit();

        scores = GameActivity.loginPreferences.getStringSet("score",null);
        if(scores == null)
            scores = new HashSet<String>();
        String[] values =scores.toArray(new String[scores.size()]);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.list_item,R.id.lbl_score, values);
        lstView.setAdapter(adapter);
    }
}
