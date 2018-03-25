package com.example.lap.lecture_4;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class TutorialActivity extends AppCompatActivity {
    private ArrayList<String> strings;
    private int i;
    private Button btnBack, btnNext;
    private TextView txtTutorial;
    private ImageView img;
    private TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        initComponents();
        setClickListeners();
        checkComponents(i);

    }

    private void initComponents()
    {
        btnBack = (Button)findViewById(R.id.btnBack);
        btnNext = (Button)findViewById(R.id.btnNext);
        txtTutorial = (TextView)findViewById(R.id.txt_tutorial);
        img = (ImageView)findViewById(R.id.imgView);
        tts =  new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                tts.setLanguage(Locale.US);
            }
        });
        i = 0;
        strings = new ArrayList<String>();
        strings.add("Press on Calendar Picture");
        strings.add("Wait for Microphone icon to appear Then say your event");
        strings.add("First say Event Name\nThen Day name\nThen Date in any format\nThen Time in A.M or P.M Format\n" +
                "See Sample below");
        strings.add("To hear a sample press on the Sample text below");
    }

    private void setClickListeners()
    {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                i--;
                checkComponents(i);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i++;
                checkComponents(i);
            }
        });
    }
    private void checkComponents(int i)
    {
        switch (i)
        {
            case 0:
                btnBack.setVisibility(View.INVISIBLE);
                btnBack.setEnabled(false);
                img.setVisibility(View.VISIBLE);
                img.setOnClickListener(null);
                img.setImageResource(R.drawable.calendar);
                break;
            case 1:
                btnBack.setVisibility(View.VISIBLE);
                btnBack.setEnabled(true);
                btnNext.setVisibility(View.VISIBLE);
                btnNext.setEnabled(true);
                img.setImageResource(R.drawable.voice);
                img.setOnClickListener(null);
                break;
            case 2:
                btnBack.setVisibility(View.VISIBLE);
                btnBack.setEnabled(true);
                btnNext.setVisibility(View.VISIBLE);
                btnNext.setEnabled(true);
                img.setImageResource(R.drawable.sample);
                img.setOnClickListener(null);
                tts.stop();
                break;
            case 3:
                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tts.speak("team lunch, Monday, March 26, 2018 at 7:30 pm",TextToSpeech.QUEUE_ADD, null);
                    }
                });
                btnNext.setVisibility(View.INVISIBLE);
                btnNext.setEnabled(false);
                btnBack.setVisibility(View.VISIBLE);
                btnBack.setEnabled(true);
                img.setImageResource(R.drawable.sample);
                break;
        }
        txtTutorial.setText(strings.get(i));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        initComponents();
        setClickListeners();
        i = (Integer)savedInstanceState.getInt("index",0);
        checkComponents(i);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("index",i);
    }

}
