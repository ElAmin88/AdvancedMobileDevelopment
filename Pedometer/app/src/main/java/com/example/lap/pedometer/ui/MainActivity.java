package com.example.lap.pedometer.ui;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lap.pedometer.R;
import com.example.lap.pedometer.classes.BaseActivity;
import com.example.lap.pedometer.classes.StepDetector;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity {
    private ArcProgress stepsProgress, caloriesProgress, distanceProgress, speedProgress;
    private StepDetector stepDetector;
    private TextView txtDuration;
    private ImageButton btn_start, btn_stop;
    private int numSteps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        setClickListeners();



    }

    private void initComponents()
    {
        stepsProgress = (ArcProgress) findViewById(R.id.steps_progress);
        stepsProgress.setMax(1000);
        stepsProgress.setSuffixText("");

        caloriesProgress =(ArcProgress)findViewById(R.id.calories_progress);
        caloriesProgress.setMax(1000);
        caloriesProgress.setSuffixText("");

        distanceProgress = (ArcProgress)findViewById(R.id.distance_progress);
        distanceProgress.setMax(1000);
        distanceProgress.setSuffixText("M");

        speedProgress = (ArcProgress)findViewById(R.id.speed_progress);
        speedProgress.setMax(100);
        speedProgress.setSuffixText(" M/Min.");

        btn_start = (ImageButton)findViewById(R.id.btn_start);
        btn_stop = (ImageButton)findViewById(R.id.btn_stop);

        txtDuration = (TextView)findViewById(R.id.txtDuration);
        stepDetector = new StepDetector(this, stepsProgress, caloriesProgress, distanceProgress, speedProgress, txtDuration);


    }

    private void setClickListeners()
    {
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepDetector.registerSensor();
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stepDetector.unregisterSensor();
            }
        });

    }


}
