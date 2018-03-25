package com.example.lap.lecture_4;

import android.content.Intent;
import android.database.Cursor;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lap.lecture_4.classes.CalendarProvider;
import com.example.lap.lecture_4.classes.Event;
import com.example.lap.lecture_4.classes.EventCustomAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class EventActivity extends AppCompatActivity {
    private CalendarProvider calendarProvider;
    private ListView eventListView;
    private  TextToSpeech tts;
    private EventCustomAdapter adapter;
    private long clickedItemID;
    private Timer timer;
    private ArrayList <Event>events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        initComponents();
        setClickListeners();
    }


    public void initComponents()
    {
        calendarProvider = new CalendarProvider(this);
        eventListView =(ListView)findViewById(R.id.eventListView);
        events = calendarProvider.getAllEvents();
        adapter = new EventCustomAdapter ( this,events);
        eventListView.setAdapter(adapter);
        clickedItemID = -1;
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                tts.setLanguage(Locale.US);
            }
        });
    }

    public void setClickListeners()
    {
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Event e = (Event) eventListView.getItemAtPosition(i);
                tts.stop();
                if(clickedItemID == e.getEventId())
                {
                    calendarProvider.deleteEventById(e.getEventId());
                    events.remove(e);
                    clickedItemID = -1;
                    adapter.notifyDataSetChanged();
                    tts.stop();
                }
                else {
                    tts.speak(e.toString(), TextToSpeech.QUEUE_ADD, null);
                    clickedItemID = e.getEventId();
                    startTimer();
                }
            }
        });
    }




    private void startTimer()
    {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            int i = 0;
            @Override
            public void run() {
                i+=100;
                if(i%1000 == 0){
                    clickedItemID = -1;
                    timer.cancel();
                }
            }
        },0,100);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(tts != null)
            tts.stop();
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
        finish();
    }


}
