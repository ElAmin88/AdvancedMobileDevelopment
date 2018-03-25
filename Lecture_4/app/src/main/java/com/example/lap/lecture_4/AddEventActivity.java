package com.example.lap.lecture_4;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.lap.lecture_4.classes.CalendarProvider;

import java.util.Calendar;
import java.util.Locale;

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
        editTextdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
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
                event = editTextevent.getText().toString();
                if(day.equals("") || month.equals("") || year.equals("") || time.equals("") || event.equals(""))
                    Toast.makeText(getBaseContext(),"Please fill all fields", Toast.LENGTH_LONG).show();
                else {
                    calendarProvider.AddEvent(day, month, year, time, event);
                    Intent i = new Intent(getBaseContext(), EventActivity.class);
                    startActivity(i);
                }

            }
        });

        editTexttime.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    int hour =0, minute = 0;
                    if(time == "")
                    {
                        Calendar mcurrentTime = Calendar.getInstance();
                        hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        minute = mcurrentTime.get(Calendar.MINUTE);
                    }

                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(AddEventActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            if(selectedHour <12 )
                            {
                                time = selectedHour + ":" + selectedMinute+" a.m.";
                            }
                            else
                            {
                                time= selectedHour-12 + ":" + selectedMinute+" p.m.";
                            }
                            editTexttime.setText(time);
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();
                }
                return true;
            }
        });

        editTextdate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                {
                    int y,m,d;
                    try {
                        y = Integer.parseInt(year);
                        m = Integer.parseInt(month);
                        d = Integer.parseInt(day);
                    }catch (Exception e)
                    {
                        Calendar c = Calendar.getInstance();
                        y = c.get(Calendar.YEAR);
                        m = c.get(Calendar.MONTH);
                        d = c.get(Calendar.DAY_OF_MONTH);
                    }
                    DatePickerDialog datePickerDialog = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int y, int m, int d) {

                            Calendar c = Calendar.getInstance();
                            c.set(y, m, d);
                            dayOfWeek = c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                            month = c.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                            day = d+"";
                            year = y+"";
                            editTextdate.setText(dayOfWeek + " " + month + " " + day + ", " + year);
                        }
                    }, y, m, d);
                    datePickerDialog.show();
                }

                return true;
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
        if(day.equals(""))
            editTextdate.setText("");
    }

    }
