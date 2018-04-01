package com.example.lap.pedometer.classes;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.TextView;

import com.example.lap.pedometer.R;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lap on 3/30/2018.
 */

public class StepDetector {
    private SensorManager sensorManager;
    private Sensor sensor;
    private int sensorStatus, numSteps, duration;
    private float calories, distance, speed, stepLength;
    private Context ctx;
    private ArcProgress stepProgress, caloriesProgress, distanceProgress, speedProgress;
    private TextView txtDuration;
    private Timer timer;

    public StepDetector(Context context, ArcProgress stepProgress, ArcProgress caloriesProgress, ArcProgress distanceProgress,
                        ArcProgress speedProgress, TextView txtDuration)
    {
        ctx = context;
        sensorManager = (SensorManager)ctx.getSystemService(ctx.SENSOR_SERVICE);
        sensorStatus = checkSensors();
        sensor = sensorManager.getDefaultSensor(sensorStatus);

        duration = 0;
        numSteps = 0;
        stepLength = (float)75.5;
        distance = 0;
        speed = 0;

        this.stepProgress = stepProgress;
        this.caloriesProgress = caloriesProgress;
        this.distanceProgress =distanceProgress;
        this.speedProgress = speedProgress;
        this.txtDuration = txtDuration;

        stepProgress.setSuffixText("");
        stepProgress.setMax(1000);
    }

    public void registerSensor()
    {
        startTimer();
        sensorManager.registerListener(new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent sensorEvent) {
                float[] values;
                if(sensorStatus == Sensor.TYPE_STEP_DETECTOR) {
                    values = sensorEvent.values;
                    if(values[0] == 1.0)
                    {
                        numSteps++;
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

    private void startTimer()
    {
        timer= new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //distance in Kilometers
                distance = (numSteps * stepLength) / 1000;
                //duration in seconds
                duration++;
                updateViews();



            }
        },0,1000);


    }

    private void updateViews()
    {
        stepProgress.post(new Runnable() {
            @Override
            public void run() {
                if(numSteps >= 1000)
                {
                    stepProgress.setSuffixText(" K");
                    stepProgress.setProgress(numSteps/1000);
                }
                else if(numSteps >= 1000000)
                {

                    stepProgress.setSuffixText(" M");
                    stepProgress.setProgress(numSteps/1000000);
                }
                else {
                    stepProgress.setProgress(numSteps);
                }
            }
        });

        distanceProgress.post(new Runnable() {
            @Override
            public void run() {
                distanceProgress.setProgress((int)distance);
            }
        });

        txtDuration.post(new Runnable() {
            @Override
            public void run() {
                if(duration%60 > 1)
                    txtDuration.setText(duration/60+"");
                if (duration % 120 > 1)
                    txtDuration.setText(duration/120+"");
            }
        });

    }
}
