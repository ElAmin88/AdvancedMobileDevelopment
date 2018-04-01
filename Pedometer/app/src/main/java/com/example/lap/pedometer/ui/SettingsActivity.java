package com.example.lap.pedometer.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lap.pedometer.R;
import com.example.lap.pedometer.classes.BaseActivity;
import com.example.lap.pedometer.classes.DatabaseAdapter;
import com.example.lap.pedometer.classes.User;

public class SettingsActivity extends BaseActivity {
    private EditText input_Weight, input_Height, input_StepLength;
    private Button btn_confirm;
    private User currentUser;
    private float weight, height, stepLength;
    private DatabaseAdapter databaseAdapter;
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

        input_Height = (EditText)findViewById(R.id.input_Height);
        input_Height.setText(currentUser.getHeight()+"");

        input_Weight = (EditText)findViewById(R.id.input_Weight);
        input_Weight.setText(currentUser.getWeight()+"");

        input_StepLength = (EditText)findViewById(R.id.input_StepLength);
        input_StepLength.setText(currentUser.getStepLegth()+"");
        if(currentUser.getStepLegth() == 0)
            input_StepLength.setEnabled(false);

        btn_confirm = (Button)findViewById(R.id.btn_confirm);

        databaseAdapter = new DatabaseAdapter(this);
        databaseAdapter.open();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseAdapter.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        databaseAdapter.close();
        finish();
    }

    private void setClickListeners()
    {
        TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event != null &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if (event == null || !event.isShiftPressed()) {
                        if (validate(false))
                        {
                            height = Float.parseFloat(input_Height.getText().toString());
                            weight = Float.parseFloat(input_Weight.getText().toString());
                            stepLength = (float) (height * 0.45);
                            input_StepLength.setText(stepLength+"");
                            input_StepLength.setEnabled(true);
                        }

                        return true; // consume.
                    }
                }
                    return false;
            }
        };

        input_Height.setOnEditorActionListener(editorActionListener);
        input_Weight.setOnEditorActionListener(editorActionListener);

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
    }

    private  void update()
    {
        if (validate(true))
        {
            databaseAdapter.updateUserInformation(currentUser, weight, height, stepLength);
            SplashActivity.setCurrentUser(databaseAdapter.getUserByName(currentUser.getName()));
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
