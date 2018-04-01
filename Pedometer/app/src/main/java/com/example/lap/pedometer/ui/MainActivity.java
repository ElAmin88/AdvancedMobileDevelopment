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
import android.widget.TextView;

import com.example.lap.pedometer.R;
import com.example.lap.pedometer.classes.BaseActivity;
import com.example.lap.pedometer.classes.StepDetector;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends BaseActivity {
    private ArcProgress stepsProgress, caloriesProgress, distanceProgress, speedProgress;
    private StepDetector stepDetector;
    TextView txtDuration;
    private int numSteps = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stepsProgress = (ArcProgress) findViewById(R.id.steps_progress);
        caloriesProgress =(ArcProgress)findViewById(R.id.calories_progress);
        distanceProgress = (ArcProgress)findViewById(R.id.distance_progress);
        speedProgress = (ArcProgress)findViewById(R.id.speed_progress);
        txtDuration = (TextView)findViewById(R.id.txtDuration);
        stepDetector = new StepDetector(this, stepsProgress, caloriesProgress, distanceProgress, speedProgress, txtDuration);
        stepDetector.registerSensor();



    }

    private void takeScreenshot()
    {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";

            // create bitmap screen capture
            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }
}
