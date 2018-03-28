package com.example.lap.lecture_4;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lap.lecture_4.classes.CalendarProvider;
import com.example.lap.lecture_4.classes.HelpDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private ImageButton btnRecord;
    private Button btnUpcomingEvents, btnOptions, btnHelp, btnExit;
    private HelpDialog helpDialog;
    private final int CHECK_TTS_DATA = 1;
    private final int CHECK_STT_DATA = 0;
    private String event = null, day = null, month = null, year = null, dayOfWeek = null, time = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        setClickListeners();
        checkTTS();
        checkSTT();
    }

    //Initialization all the variables
    private void initComponents()
    {
        btnRecord = (ImageButton)findViewById(R.id.btnRecord);
        btnUpcomingEvents = (Button)findViewById(R.id.btnUpcomingEvents);
        btnOptions = (Button)findViewById(R.id.btnOptions);
        btnHelp = (Button)findViewById(R.id.btnHelp);
        btnExit = (Button)findViewById(R.id.btnExit);
        helpDialog = new HelpDialog(this);
    }

    private  void setClickListeners()
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
        btnOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), OptionsActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helpDialog.show();
            }
        });

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                System.exit(0);
            }
        });
    }

    //Starting the voice recognition Intent
    public void Record()
    {
        Intent intent = new
                Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak!");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.ENGLISH);
        startActivityForResult(intent, CHECK_STT_DATA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHECK_TTS_DATA) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {

                List<String> available = data.getStringArrayListExtra(TextToSpeech.Engine.EXTRA_AVAILABLE_VOICES);
                List<String> unavailable = data.getStringArrayListExtra(TextToSpeech.Engine.EXTRA_UNAVAILABLE_VOICES);
            }
            else {
                String action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA;
                Intent install = new Intent();
                install.setAction(action);
                startActivity(install);
            }
        }

        else if (requestCode == CHECK_STT_DATA) {
            String pattern ="(Sunday | Monday | Tuesday | Wednesday | Thursday | Friday | Saturday)";
            if (resultCode == -1) {
                ArrayList<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
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
        String []res = date.split(" at ");
        if(res.length > 1 && res[1] != "")
        {
            if(res[1].contains("a.m.") || res[1].contains("p.m."))
                return res[1];
            else
            {
                Toast.makeText(getBaseContext(),"You didn't specify Time A.M. or P.M.",Toast.LENGTH_LONG).show();
                return null;
            }
        }
        Toast.makeText(getBaseContext(),"You didn't specify Time",Toast.LENGTH_LONG).show();
        return null;
    }

    //Checking if Text-To-Speech available or not
    private void checkTTS()
    {
        Intent intent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent, CHECK_TTS_DATA);
    }

    //Checking if Speech-To-Text available or not
    private void checkSTT()
    {
        PackageManager pm = getPackageManager();
        List activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
        if (activities.size() != 0) {
            setClickListeners();
        }
        else {
            btnRecord.setEnabled(false);
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        System.exit(0);
    }
}
