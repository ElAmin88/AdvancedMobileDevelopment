package com.example.lap.pedometer.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lap.pedometer.R;
import com.example.lap.pedometer.classes.BaseActivity;
import com.example.lap.pedometer.classes.DatabaseAdapter;
import com.example.lap.pedometer.classes.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends BaseActivity {
    private EditText input_Weight, input_Height, input_StepLength;
    private Button btn_confirm, btn_addPicture;
    private User currentUser;
    private float weight, height, stepLength;
    private DatabaseAdapter databaseAdapter;
    private final int RESULT_LOAD_IMAGE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initComponents();
        setClickListeners();

    }

    private void initComponents()
    {
        currentUser = SplashActivity.getCurrentUser();
        btn_addPicture = (Button)findViewById(R.id.btn_addPicture);

        input_Height = (EditText)findViewById(R.id.input_Height);
        input_Height.setText(currentUser.getHeight()+"");

        input_Weight = (EditText)findViewById(R.id.input_Weight);
        input_Weight.setText(currentUser.getWeight()+"");

        input_StepLength = (EditText)findViewById(R.id.input_StepLength);
        input_StepLength.setText(currentUser.getStepLegth()+"");

        btn_confirm = (Button)findViewById(R.id.btn_confirm);

        databaseAdapter = new DatabaseAdapter(this);
        databaseAdapter.open();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseAdapter.close();
    }


    private void setClickListeners()
    {

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

        btn_addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);


            if (cursor == null || cursor.getCount() < 1) {
                return; // no cursor or no record. DO YOUR ERROR HANDLING
            }

            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

            if(columnIndex < 0) // no column index
                return; // DO YOUR ERROR HANDLING

            String picturePath = cursor.getString(columnIndex);

            cursor.close(); // close cursor
            Bitmap picture = BitmapFactory.decodeFile(picturePath.toString());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            picture.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            currentUser.setPicture(stream.toByteArray());
        }


    }

    private  void update()
    {
        if (validate(true))
        {
            weight = Float.parseFloat(input_Weight.getText().toString());
            height = Float.parseFloat(input_Height.getText().toString());
            stepLength = Float.parseFloat(input_StepLength.getText().toString());

            currentUser.setWeight(weight);
            currentUser.setStepLegth(stepLength);
            currentUser.setHeight(height);
            int b = databaseAdapter.updateUserInformation(currentUser, weight, height, stepLength, currentUser.getPicture());
            if(b ==1)
                SplashActivity.setCurrentUser(currentUser);
            Intent i = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(i);
            finish();
        }
    }

    private boolean validate(boolean all)
    {

        if(input_Weight.getText().equals("") || input_Weight.getText().equals("0"))
        {
            input_Weight.setError("Please enter a valid Number");
            return false;
        }
        if(input_Height.getText().equals("") || input_Height.getText().equals("0"))
        {
            input_Height.setError("Please enter a valid Number");
            return false;
        }
        if(all)
        {
            if(input_StepLength.getText().equals("") || input_StepLength.getText().equals("0"))
                return false;

        }
        return true;
    }

}
