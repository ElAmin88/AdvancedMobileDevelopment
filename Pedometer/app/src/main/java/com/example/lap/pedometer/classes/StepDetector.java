package com.example.lap.pedometer.classes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import com.example.lap.pedometer.R;
import com.github.lzyzsd.circleprogress.DonutProgress;

/**
 * Created by lap on 3/30/2018.
 */

public class StepDetector {
    private SensorManager sensorManager;
    private Sensor sensor;
    private int sensorStatus, numSteps;
    private Context ctx;
    private DonutProgress donutProgress;

    public StepDetector(Context context, DonutProgress d)
    {
        ctx = context;
        sensorManager = (SensorManager)ctx.getSystemService(ctx.SENSOR_SERVICE);
        sensorStatus = checkSensors();
        sensor = sensorManager.getDefaultSensor(sensorStatus);
        numSteps = 0;
        donutProgress = d;
        donutProgress.setSuffixText("");
        donutProgress.setMax(1000);
    }

    public void registerSensor()
    {

        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float[] values;
                if(sensorStatus == Sensor.TYPE_STEP_DETECTOR) {
                    values = sensorEvent.values;
                    if(values[0] == 1.0)
                    {
                        numSteps++;
                        if(numSteps >= 1000)
                        {
                            donutProgress.setSuffixText(" K");
                            donutProgress.setProgress(numSteps/1000);
                        }
                        else if(numSteps >= 1000000)
                        {
                            donutProgress.setSuffixText(" M");
                            donutProgress.setProgress(numSteps/1000000);
                        }
                        else {
                            donutProgress.setProgress(numSteps);
                        }
                    }
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

    public int getNumSteps() {
        return numSteps;
    }

    public void setNumSteps(int numSteps) {
        this.numSteps = numSteps;
    }
}
