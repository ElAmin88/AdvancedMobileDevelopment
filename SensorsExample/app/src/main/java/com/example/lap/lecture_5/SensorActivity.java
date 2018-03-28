package com.example.lap.lecture_5;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SensorActivity extends AppCompatActivity {

    int sensorType;
    Sensor sensor;
    SensorManager sensorManager;
    TextView lbl_X,lbl_Y,lbl_Z;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        initComponents();
        setClickListners();
    }

    private void initComponents()
    {
        sensorType = getIntent().getIntExtra("type", 1);
        sensorManager = (SensorManager)getSystemService(this.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensorType);
        lbl_X = (TextView)findViewById(R.id.lbl_XValue);
        lbl_Y = (TextView)findViewById(R.id.lbl_YValue);
        lbl_Z = (TextView)findViewById(R.id.lbl_ZValue);

    }

    private void setClickListners()
    {
        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float [] values = sensorEvent.values;
                lbl_X.setText(values[0]+"");
                lbl_Y.setText(values[1]+"");
                lbl_Z.setText(values[2]+"");

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        },sensor,sensorManager.SENSOR_DELAY_NORMAL);
    }
}
