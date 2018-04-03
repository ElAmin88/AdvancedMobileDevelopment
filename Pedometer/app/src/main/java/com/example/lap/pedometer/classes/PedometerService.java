package com.example.lap.pedometer.classes;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.lap.pedometer.ui.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by lap on 4/2/2018.
 */

public class PedometerService extends Service {
    private StepDetector stepDetector;
    private Timer timer;
    private int duration, numSteps;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (MainActivity.running) {
            MainActivity.fromService = true;
            MainActivity.waitingResults = true;
            Log.d("Service","service running");
            stepDetector = new StepDetector(this);
            numSteps = intent.getIntExtra("numSteps",-1);
            stepDetector.setNumSteps(numSteps);
            duration = intent.getIntExtra("duration",-1);
            if (numSteps > 0 && duration > 0) {
                stepDetector.registerSensor();
                timer= new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {

                        //distance in CMs
                        //duration in seconds
                        duration++;

                    }
                },0,1000);
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }


    @Override
    public void onDestroy() {
        Log.d("Service", "Service Stopped");
        if (MainActivity.running) {
            Intent i = new Intent();
            i.setAction("Service");
            stepDetector.unregisterSensor();
            i.putExtra("numSteps", stepDetector.getNumSteps());
            i.putExtra("duration", duration);
            sendBroadcast(i);
            MainActivity.waitingResults = false;
        }
        super.onDestroy();

    }
}
