package com.example.lap.pedometer.ui;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;

import com.example.lap.pedometer.R;
import com.example.lap.pedometer.classes.BaseActivity;
import com.example.lap.pedometer.classes.StepDetector;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity {
    private DonutProgress donutProgress;
    private StepDetector stepDetector;
    private int numSteps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        donutProgress = (DonutProgress) findViewById(R.id.donut_progress);
        stepDetector = new StepDetector(this, donutProgress);
        stepDetector.registerSensor();



    }
}
