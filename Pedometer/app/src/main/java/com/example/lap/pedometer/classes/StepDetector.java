package com.example.lap.pedometer.classes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import java.io.Serializable;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lap on 3/30/2018.
 */

public class StepDetector implements StepListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private int sensorStatus, numSteps;
    private Context ctx;
    private SensorEventListener sensorEventListener;
    private SimpleStepDetector simpleStepDetector;

    public StepDetector(Context context)
     {
        ctx = context;
        sensorManager = (SensorManager)ctx.getSystemService(ctx.SENSOR_SERVICE);
        sensorStatus = checkSensors();
        sensor = sensorManager.getDefaultSensor(sensorStatus);
        numSteps = 0;
        simpleStepDetector = new SimpleStepDetector();
        sensorEventListener = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float[] values = sensorEvent.values;
                if(sensorStatus == Sensor.TYPE_STEP_DETECTOR) {

                    if(values[0] == 1.0)
                    {
                        numSteps++;
                    }
                }
                else if (sensorStatus == Sensor.TYPE_ACCELEROMETER)
                {
                    simpleStepDetector.updateAccel(
                            sensorEvent.timestamp, values[0], values[1], values[2]);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int i) {

            }
        };
    }

    public void registerSensor()
    {
        sensorManager.registerListener(sensorEventListener,sensor,sensorManager.SENSOR_DELAY_FASTEST);
    }

    public void unregisterSensor()
    {
        sensorManager.unregisterListener(sensorEventListener,sensor);
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

    public int getNumSteps() {
        return numSteps;
    }

    public void setNumSteps(int numSteps) {
        this.numSteps = numSteps;
    }


    @Override
    public void step(long timeNs) {
        numSteps++;
    }
}
