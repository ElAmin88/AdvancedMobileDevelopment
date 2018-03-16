package com.example.lap.lecture_4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddEventActivity extends AppCompatActivity {
    Button btnDone, btnCancel;
    EditText editTextevent, editTextdate, editTexttime;
    String event, dayOfWeek, day, month, year, time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        editTextevent = (EditText)findViewById(R.id.editTextEvent);
        editTextdate = (EditText)findViewById(R.id.editTextDate);
        editTexttime = (EditText)findViewById(R.id.editTextTime);
        btnCancel = (Button)findViewById(R.id.btnCancel);
        btnDone = (Button)findViewById(R.id.btnDone);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(),MainActivity.class);
                startActivity(i);
                finish();
            }
        });
        event = getIntent().getStringExtra("Event");
        dayOfWeek = getIntent().getStringExtra("DayofWeek");
        day = getIntent().getStringExtra("Day");
        month = getIntent().getStringExtra("Month");
        year = getIntent().getStringExtra("Year");
        time = getIntent().getStringExtra("Time");

        editTextevent.setText(event);
        editTextdate.setText(dayOfWeek + " " + day  +"/" + month + "/" + year);
        editTexttime.setText(time);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getBaseContext(),MainActivity.class);
        startActivity(i);
        finish();
    }
}
