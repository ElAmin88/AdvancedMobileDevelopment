package com.example.lap.lecture_4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lap.lecture_4.classes.CalendarProvider;

public class OptionsActivity extends AppCompatActivity {
    Button btnDeleteAll, btnAddManually;
    CalendarProvider calendarProvider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        initComponents();
        setClickListeners();
    }

    private void initComponents()
    {
        btnDeleteAll = (Button)findViewById(R.id.btnDeleteAll);
        btnAddManually = (Button)findViewById(R.id.btn_addManual);
        calendarProvider = new CalendarProvider(this);
    }

    private void setClickListeners()
    {
        btnDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendarProvider.deleteAllEvents();
                Intent i = new Intent(getBaseContext(), MainActivity.class);
                Toast.makeText(getBaseContext(), "Deleted All events",Toast.LENGTH_LONG).show();
                startActivity(i);
            }
        });

        btnAddManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), AddEventActivity.class);
                i.putExtra("DayofWeek","");
                i.putExtra("Day", "");
                i.putExtra("Month", "");
                i.putExtra("Year", "");
                i.putExtra("Event","");
                i.putExtra("Time","");
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
        finish();
    }
}
