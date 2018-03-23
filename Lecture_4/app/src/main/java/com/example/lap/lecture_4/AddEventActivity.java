package com.example.lap.lecture_4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lap.lecture_4.classes.CalendarProvider;

public class AddEventActivity extends AppCompatActivity {
    private Button btnDone, btnCancel;
    private EditText editTextevent, editTextdate, editTexttime;
    private String event, dayOfWeek, day, month, year, time;
    private CalendarProvider calendarProvider;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        initComponents();
        setClickListeners();
        getIntentData();

        editTextevent.setText(event);
        editTextdate.setText(dayOfWeek + " " + month + " " + day + ", " + year);
        editTexttime.setText(time);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    private void initComponents() {
        editTextevent = (EditText) findViewById(R.id.editTextEvent);
        editTextdate = (EditText) findViewById(R.id.editTextDate);
        editTexttime = (EditText) findViewById(R.id.editTextTime);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnDone = (Button) findViewById(R.id.btnDone);
        calendarProvider = new CalendarProvider(this);
    }

    private void setClickListeners() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarProvider.AddEvent(day, month, year, time, event);
                Intent i = new Intent(getBaseContext(),EventActivity.class);
                startActivity(i);
            }
        });
    }

    private void getIntentData() {
        event = getIntent().getStringExtra("Event");
        dayOfWeek = getIntent().getStringExtra("DayofWeek");
        day = getIntent().getStringExtra("Day");
        month = getIntent().getStringExtra("Month");
        year = getIntent().getStringExtra("Year");
        time = getIntent().getStringExtra("Time");
    }

    }
