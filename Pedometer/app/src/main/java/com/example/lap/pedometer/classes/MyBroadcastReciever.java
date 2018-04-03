package com.example.lap.pedometer.classes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lap on 4/2/2018.
 */

public class MyBroadcastReciever extends BroadcastReceiver {
    private int numSteps = 0, duration = 0;
    public boolean recieved = false;
    @Override
    public void onReceive(Context context, Intent intent) {
        numSteps = intent.getIntExtra("numSteps", 0);
        duration = intent.getIntExtra("duration",0);
        recieved = true;
    }

    public int getDuration() {
        return duration;
    }

    public int getNumSteps() {
        return numSteps;
    }
}
