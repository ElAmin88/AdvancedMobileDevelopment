package com.example.lap.pedometer.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lap.pedometer.R;
import com.example.lap.pedometer.classes.BaseActivity;
import com.example.lap.pedometer.classes.DatabaseAdapter;
import com.example.lap.pedometer.classes.RunRecord;
import com.example.lap.pedometer.classes.User;

import java.util.ArrayList;

public class ProfileActivity extends BaseActivity {

    private TextView lbl_weight, lbl_height, lbl_runs, lbl_calories;
    private ImageView img_profilePicture;
    private  float weight, height, runs, calories;
    private User currentUser;
    DatabaseAdapter databaseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initComponents();

    }

    private void initComponents()
    {
        databaseAdapter = new DatabaseAdapter(this);
        databaseAdapter.open();
        currentUser = SplashActivity.getCurrentUser();

        getUserDetails();

        lbl_weight = (TextView)findViewById(R.id.lbl_weightValue);
        lbl_weight.setText(weight+"");

        lbl_height = (TextView)findViewById(R.id.lbl_heightValue);
        lbl_height.setText(height+"");

        lbl_runs = (TextView)findViewById(R.id.lbl_totalRunsValue);
        lbl_runs.setText((int)runs+"");

        lbl_calories = (TextView)findViewById(R.id.lbl_totalCaloriesValue);
        lbl_calories.setText(calories+"");

        img_profilePicture = (ImageView)findViewById(R.id.img_profilePicture);
        Bitmap img;
        byte[] emptyArray = new byte[0];
        if(currentUser.getPicture() != null)
            img = BitmapFactory.decodeByteArray(currentUser.getPicture(),0,currentUser.getPicture().length);
        else
            img = BitmapFactory.decodeByteArray(emptyArray,0,0);
        img_profilePicture.setImageBitmap(img);


    }

    private void getUserDetails()
    {
        weight = currentUser.getWeight();
        height = currentUser.getHeight();
        ArrayList <RunRecord> records = databaseAdapter.getRunRecordsofUser(currentUser.getId());
        runs = records.size();
        calories = 0;
        for (int i =0; i < records.size(); i++)
        {
            calories += records.get(i).getNumCalories();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseAdapter.close();
    }


}
