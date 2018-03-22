package com.example.lap.lecture_4;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.CalendarContract;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EventActivity extends AppCompatActivity {
    Cursor cursor;
    ListView eventListView;
    final int CHECK_TTS_DATA = 1;
    private  TextToSpeech tts;
    ArrayList<String> events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        initComponents();
        setClickListners();
    }


    public void initComponents()
    {
        eventListView =(ListView)findViewById(R.id.eventListView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String> ( this, android.R.layout.simple_list_item_1, android.R.id.text1, getEvents());
        eventListView.setAdapter(adapter);
        CheckTTS();
    }

    public void setClickListners()
    {
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String e = (String)eventListView.getItemAtPosition(i);
                speakEvent(e);
            }
        });
    }

    private ArrayList<String> getEvents()
    {
        ArrayList<String> events = new ArrayList<String>();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return events;
        }
        cursor = getContentResolver().query(CalendarContract.Events.CONTENT_URI, null, null, null, null);
        while (cursor.moveToNext())
        {
            if (cursor != null)
            {
                int idIDX = cursor.getColumnIndex(CalendarContract.Events._ID);
                int titleIDX = cursor.getColumnIndex(CalendarContract.Events.TITLE);
                int descriptionIDX = cursor.getColumnIndex(CalendarContract.Events.DESCRIPTION);
                int locationIDX = cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION);
                int startIDX = cursor.getColumnIndex(CalendarContract.Events.DTSTART);
                int endIDX = cursor.getColumnIndex(CalendarContract.Events.DTEND);

                String id = cursor.getString(idIDX);
                String title =  cursor.getString(titleIDX);
                //String description = cursor.getString(descriptionIDX);
                //String location = cursor.getString(locationIDX);
                String start = cursor.getString(startIDX);
                String end = cursor.getString(endIDX);
                String startDate ="";
                String endDate = "";
                try {
                    long s = Long.parseLong(start);
                    long e = Long.parseLong(end);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(s);
                    startDate = calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.YEAR) ;
                    calendar.setTimeInMillis(e);
                    endDate = calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.YEAR);

                }catch (Exception e){}
                String e ="Event name: "+title+"\n\r Start Date: "+startDate+"\n\r End Date: "+endDate;
                events.add(e);

            }

        }
        return events;
    }

    private void CheckTTS()
    {
        Intent intent = new Intent(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(intent, CHECK_TTS_DATA);
    }

    private void speakEvent(final String event)
    {
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS) {
                    String msg = event;
                    tts.speak(msg, TextToSpeech.QUEUE_ADD, null);
                }
            }
        });
        tts.setLanguage(Locale.US);
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        tts.stop();
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
        finish();
    }


}
