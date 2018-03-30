package com.example.lap.pedometer.classes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import com.example.lap.pedometer.R;

/**
 * Created by lap on 3/30/2018.
 */

public class StepDetector {
    private SensorManager sensorManager;
    private Sensor sensor;
    private int sensorStatus, numSteps;
    private Context ctx;

    public StepDetector(Context context)
    {
        ctx = context;
        sensorManager = (SensorManager)ctx.getSystemService(ctx.SENSOR_SERVICE);
        sensorStatus = checkSensors();
        sensor = sensorManager.getDefaultSensor(sensorStatus);
        numSteps = 0;
    }

    private void registerSensor(final int sensorStatus)
    {

        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float[] values;
                if(sensorStatus == Sensor.TYPE_STEP_DETECTOR) {
                    values = sensorEvent.values;
                    if(values[0] == 1.0)
                        numSteps++;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        },sensor,sensorManager.SENSOR_DELAY_FASTEST);
    }


    private int checkSensors()
    {
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (sensor != null)
            return Sensor.TYPE_STEP_DETECTOR;
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (sensor != null)
            return Sensor.TYPE_ACCELEROMETER;
        return 0;
    }
}
