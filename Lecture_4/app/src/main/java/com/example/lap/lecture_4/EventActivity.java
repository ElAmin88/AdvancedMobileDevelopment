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

import java.util.List;
import java.util.Locale;

public class EventActivity extends AppCompatActivity {
    Cursor cursor;
    CalendarProvider calendarProvider;
    ListView eventListView;
    final int CHECK_TTS_DATA = 1;
    private  TextToSpeech tts;
    EventCustomAdapter adapter;
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
        adapter = new EventCustomAdapter ( this, calendarProvider.getAllEvents());
        eventListView.setAdapter(adapter);
        CheckTTS();
    }

    public void setClickListeners()
    {
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Event e = (Event) eventListView.getItemAtPosition(i);
                speakEvent(e.toString());
            }
        });
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
        if(tts != null)
            tts.stop();
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
        finish();
    }


}
