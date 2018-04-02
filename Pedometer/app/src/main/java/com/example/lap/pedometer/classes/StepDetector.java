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
    private boolean pause;
    private SensorEventListener sensorEventListener;

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

        pause = false;

        sensorEventListener = new SensorEventListener() {

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
        };
    }

    public void registerSensor()
    {
        startTimer();
        sensorManager.registerListener(sensorEventListener,sensor,sensorManager.SENSOR_DELAY_FASTEST);
    }

    public void unregisterSensor()
    {
        stopTimer();
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

    public void startTimer()
    {
        timer= new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!pause)
                {
                    //distance in CMs
                    distance = (numSteps * stepLength);
                    //duration in seconds
                    duration++;
                    updateViews();
                }

            }
        },0,1000);


    }

    private void stopTimer()
    {
        timer.cancel();
    }

    public void pauseTimer()
    {
        pause = true;
    }

    public void resumeTimer()
    {
        pause = false;
    }

    private void updateViews()
    {
        updateNumSteps();
        updateDistance();
        updateDuration();
        updateSpeed();

    }

    private void updateDistance()
    {
        final float km, m;
        m = distance/100;
        km = m/1000;

        distanceProgress.post(new Runnable() {
            @Override
            public void run() {
                if(m < 1000) {
                    distanceProgress.setMax(1000);
                    distanceProgress.setProgress((int) m);
                    distanceProgress.setSuffixText(" M");
                }
                else
                {
                    distanceProgress.setMax(100);
                    distanceProgress.setProgress((int)km);
                    distanceProgress.setSuffixText(" KM");
                }
            }
        });
    }

    private void updateCalories()
    {

    }

    private void updateSpeed()
    {
        final float minute, meter, km , hour;
        meter = distance/100;
        km = meter / 1000;
        minute = duration/60;
        hour = minute/60;

        speedProgress.post(new Runnable() {
            @Override
            public void run() {
                if(meter > 100)
                {
                    int pace = (int)(meter/minute); //pace in meter/minute
                    speedProgress.setSuffixText(" M/Min.");
                    speedProgress.setProgress(pace);
                }
                else if(km > 1)
                {
                    int pace = (int)(km/hour); //pace in km/hour
                    speedProgress.setSuffixText(" KM/H");
                    speedProgress.setProgress(pace);
                }
            }
        });
    }

    private void updateNumSteps()
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
    }

    private void updateDuration()
    {
        final int h, m;
        m = duration/60;
        h = m/60;

        txtDuration.post(new Runnable() {
            @Override
            public void run() {
                if(m<10)
                    txtDuration.setText(h + ":0" + m + "," + duration%60);
                else
                    txtDuration.setText(h + ":" + m + "," + duration%60);
            }
        });

    }
}
