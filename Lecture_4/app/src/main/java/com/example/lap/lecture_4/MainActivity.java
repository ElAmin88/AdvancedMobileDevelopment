package com.example.lap.lecture_4;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.media.MediaRecorder.AudioSource.VOICE_RECOGNITION;

public class MainActivity extends AppCompatActivity {
    ImageButton btnRecord;
    Button btnUpcomingEvents;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();

        PackageManager pm = getPackageManager();
        List activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            setClickListners();
        }
        else {
            btnRecord.setEnabled(false);
        }



    }

    private void initComponents()
    {
        btnRecord = (ImageButton)findViewById(R.id.btnRecord);
        btnUpcomingEvents = (Button)findViewById(R.id.btnUpcomingEvents);
    }

    private  void setClickListners()
    {
        btnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Record();
            }
        });
        btnUpcomingEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), EventActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    public void Record()
    {
        final int VOICE_RECOGNITION = 0;
        Intent intent = new
                Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak!");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.ENGLISH);
        startActivityForResult(intent, VOICE_RECOGNITION);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String pattern ="(Sunday | Monday | Tuesday | Wednesday | Thursday | Friday | Saturday)";
        String event = null, day = null, month = null, year = null, dayOfWeek = null, time = null;
        if (requestCode == 0) {
            if (resultCode == -1) {
                ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                float[] confidence = data.getFloatArrayExtra(RecognizerIntent.EXTRA_CONFIDENCE_SCORES);
                dayOfWeek = getDayofWeek(results.get(0));
                String [] res = results.get(0).split(pattern);
                if(dayOfWeek !=null && res.length > 1 ){

                    if(getDayofWeek(res[1]) == null)
                    {
                        event = res[0];
                        day = getDay(res[1]);
                        month = getMonth(res[1]);
                        year = getYear(res[1]);
                        time = getTime(res[1]);
                        if (day != null && month != null && year !=null && time != null)
                        {
                            Intent i = new Intent(getBaseContext(),AddEventActivity.class);
                            i.putExtra("DayofWeek",dayOfWeek);
                            i.putExtra("Day", day);
                            i.putExtra("Month", month);
                            i.putExtra("Year", year);
                            i.putExtra("Event",event);
                            i.putExtra("Time",time);
                            startActivity(i);
                            finish();
                        }
                    }
                    else
                        Toast.makeText(getBaseContext(),"You cannot say two different days",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(getBaseContext(),"You said wrong format please try again",Toast.LENGTH_LONG).show();
                }

            }
        }
    }

    @Nullable
    private String getDayofWeek(String result)
    {
        if (result.contains("Sunday"))
           return "Sunday";
        if (result.contains("Monday"))
            return "Monday";
        if (result.contains("Tuesday"))
            return "Tuesday";
        if (result.contains("Wednesday"))
            return "Wednesday";
        if (result.contains("Thursday"))
            return "Thursday";
        if (result.contains("Friday"))
            return "Friday";
        if (result.contains("Saturday"))
            return "Saturday";
        return null;
    }

    @Nullable
    private String getDay(String date)
    {
        String pattern = "[0-9]{1,2}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(date);
        if(m.find())
        {
            return m.group(0);

        }
        Toast.makeText(getBaseContext(),"You didn't specify day number",Toast.LENGTH_LONG).show();
        return null;
    }

    @Nullable
    private String getMonth(String date)
    {
        String pattern = "(January|February|March|April|May|June|July|August|September|October|November|December)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(date);
        if(m.find())
        {
            return m.group(0);

        }
        Toast.makeText(getBaseContext(),"You didn't specify Month",Toast.LENGTH_LONG).show();
        return null;
    }

    @Nullable
    private String getYear(String date)
    {
        String pattern = "[0-9]{4}";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(date);
        if(m.find())
        {
            return m.group(0);

        }
        Toast.makeText(getBaseContext(),"You didn't specify Year",Toast.LENGTH_LONG).show();
        return null;
    }

    @Nullable
    private String getTime(String date)
    {
        String []res = date.split("at");
        if(res.length > 1 && res[1] != "")
        {
            return res[1];
        }
        Toast.makeText(getBaseContext(),"You didn't specify Time",Toast.LENGTH_LONG).show();
        return null;
    }
}
