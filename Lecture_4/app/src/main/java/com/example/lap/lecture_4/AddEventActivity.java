package com.example.lap.lecture_4;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity {
    Button btnDone, btnCancel;
    EditText editTextevent, editTextdate, editTexttime;
    String event, dayOfWeek, day, month, year, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        initComponents();
        setClickListners();
        getIntentData();

        editTextevent.setText(event);
        editTextdate.setText(dayOfWeek + " " + day + "/" + month + "/" + year);
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
    }

    private void setClickListners() {
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
                AddEvent(day, month, year, time, event);
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

    private void AddEvent(String d, String m, String y, String time, String event) {
        int day = 0, month = 0, year = 0, hour = 0, min = 0;
        try {
            day = Integer.parseInt(d);
            month = getMonthNumber(m);
            year = Integer.parseInt(y);
            String t[] = time.split(":");
            hour = Integer.parseInt(t[0]);
            min = Integer.parseInt(t[1]);

        } catch (Exception e) {
        }
        long calendarID = 1;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(year, month, day, hour, min);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(year, month, day, hour + 1, min);
        endMillis = endTime.getTimeInMillis();

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, event);
        values.put(CalendarContract.Events.CALENDAR_ID, calendarID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
        Uri uri;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

        Intent i = new Intent(this,EventActivity.class);
        startActivity(i);
    }

    private int getMonthNumber (String month)
    {
        switch (month)
        {
            case "January":
                return 1;
            case "February":
                return 2;
            case "March":
                return 3;
            case "April":
                return 4;
            case "May":
                return 5;
            case "June":
                return 6;
            case "July":
                return 7;
            case "August":
                return 8;
            case "September":
                return 9;
            case "October":
                return 10;
            case "November":
                return 11;
            case "December":
                return 12;
            default:
                return 0;

        }
    }
}
