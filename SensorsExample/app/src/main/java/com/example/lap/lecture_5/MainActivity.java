package com.example.lap.lecture_5;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> allSensors;
    ListView sensorsListView;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();
        checkSensor();
    }

    private void initComponents()
    {
        allSensors = new ArrayList<String>();
        sensorsListView = (ListView)findViewById(R.id.lstView);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, allSensors);
        sensorsListView.setAdapter(adapter);
        setClickListeners();
    }

    private void setClickListeners()
    {
        sensorsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView v = (TextView)view;
                if(v.getText().toString().contains("Acceleration Sensor"))
                {
                    Intent intent = new Intent(getBaseContext(), SensorActivity.class);
                    intent.putExtra("type",Sensor.TYPE_ACCELEROMETER);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    private void checkSensor()
    {
        SensorManager sensorManager = (SensorManager)getSystemService(this.SENSOR_SERVICE);

        List <Sensor> sensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for(int i =0; i<sensors.size(); i++)
        {
            allSensors.add(sensors.get(i).getName());
        }
        adapter.notifyDataSetChanged();
    }
}
