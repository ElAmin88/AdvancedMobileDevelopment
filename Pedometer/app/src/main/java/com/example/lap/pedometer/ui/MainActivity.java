package com.example.lap.pedometer.ui;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lap.pedometer.R;
import com.example.lap.pedometer.classes.BaseActivity;
import com.example.lap.pedometer.classes.DatabaseAdapter;
import com.example.lap.pedometer.classes.MyBroadcastReciever;
import com.example.lap.pedometer.classes.PedometerService;
import com.example.lap.pedometer.classes.RunRecord;
import com.example.lap.pedometer.classes.StepDetector;
import com.github.lzyzsd.circleprogress.ArcProgress;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity {
    private ArcProgress stepsProgress, caloriesProgress, distanceProgress, speedProgress;
    private StepDetector stepDetector;
    private TextView txtDuration;
    private ImageButton btn_start, btn_stop;
    private float calories, distance, speed, stepLength;
    private int numSteps, stepsinMin, duration;
    private Timer timer;
    public static boolean fromService = false, running = false, waitingResults = false;
    private Intent service;
    private MyBroadcastReciever br;
    private DatabaseAdapter databaseAdapter;

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
        stepDetector = new StepDetector(this);

        databaseAdapter = new DatabaseAdapter(this);
        databaseAdapter.open();

        duration = 0;
        numSteps = 0;
        stepLength = SplashActivity.getCurrentUser().getStepLegth();
        distance = 0;
        speed = 0;
        stepsinMin = 0;

        br = new MyBroadcastReciever();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("Service");
        registerReceiver(br,intentFilter);

    }

    private void setClickListeners()
    {
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!running)
                {
                    startTimer();
                    stepDetector.registerSensor();
                }
            }
        });

        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(running)
                {
                    stepDetector.unregisterSensor();
                    stopTimer();
                    saveRecord();
                    startActivity(new Intent(getApplicationContext(), HistoryActivity.class));
                    finish();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(fromService)
        {
            stopService(service);


            stepDetector = new StepDetector(getApplicationContext());
            stepDetector.registerSensor();
            startTimer();
        }


    }


    @Override
    protected void onStop() {
        if (running)
        {
            stopTimer();
            running = true;
            service = new Intent(this, PedometerService.class);
            stepDetector.unregisterSensor();
            service.putExtra("numSteps", numSteps);
            service.putExtra("duration", duration);
            startService( service );
        }
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseAdapter.close();
    }

    public void startTimer()
    {
        running = true;
        timer= new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (br.recieved)
                {
                    numSteps = br.getNumSteps();
                    duration = br.getDuration();
                    stepDetector.setNumSteps(numSteps);
                    br.recieved = false;
                }
                //distance in CMs
                numSteps = stepDetector.getNumSteps();
                distance = (numSteps * stepLength);
                //duration in seconds
                duration++;
                updateViews();


            }
        },0,1000);


    }

    public void stopTimer()
    {
        running = false;
        timer.cancel();
    }


    private void updateViews()
    {
        updateNumSteps();
        updateDistance();
        updateDuration();
        updateSpeed();
        updateCalories();

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
        if(speed > 1)
            calories+= 0.0005 * SplashActivity.getCurrentUser().getWeight() * speed + 0.0035;
        else
            calories+= 0.0005 * SplashActivity.getCurrentUser().getWeight() * 20 + 0.0035;

        caloriesProgress.post(new Runnable() {
            @Override
            public void run() {
                caloriesProgress.setProgress((int)calories);
            }
        });
    }

    private void updateSpeed()
    {
        if(duration % 60 == 0)
        {

            final float pace = stepsinMin * stepLength;
            speed = pace / 100;
            speedProgress.post(new Runnable() {
                @Override
                public void run() {
                   speedProgress.setProgress((int)speed);
                }
            });
            stepsinMin = 0;
        }

    }

    private void updateNumSteps()
    {
        stepsProgress.post(new Runnable() {
            @Override
            public void run() {
                if(numSteps >= 1000)
                {
                    stepsProgress.setSuffixText(" K");
                    stepsProgress.setProgress(numSteps/1000);
                }
                else if(numSteps >= 1000000)
                {
                    stepsProgress.setSuffixText(" M");
                    stepsProgress.setProgress(numSteps/1000000);
                }
                else {
                    stepsProgress.setProgress(numSteps);
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

    private void saveRecord()
    {
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this,
                R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Record...");
        progressDialog.show();

        RunRecord record = new RunRecord();
        record.setUserId(SplashActivity.getCurrentUser().getId());
        record.setSpeed(speed);
        record.setNumSteps(numSteps);
        record.setNumCalories(calories);
        record.setDuration(duration);
        record.setDistance(distance);
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String time = df.format(c);
        record.setTime(time);

        databaseAdapter.AddRunRecord(record);
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                    }
                }, 2000);


    }

}
