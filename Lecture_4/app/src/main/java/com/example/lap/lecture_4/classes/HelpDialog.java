package com.example.lap.lecture_4.classes;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.lap.lecture_4.R;
import com.example.lap.lecture_4.TutorialActivity;

/**
 * Created by lap on 3/25/2018.
 */

public class HelpDialog extends Dialog implements View.OnClickListener {
    Context ctx;
    Button btnTutorial;
    public HelpDialog(@NonNull Context context) {
        super(context);
        ctx = context;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.popup_layout);
        btnTutorial = (Button)findViewById(R.id.btnTutorial);

        btnTutorial.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btnTutorial :
                ctx.startActivity(new Intent(ctx, TutorialActivity.class));
                ((Activity)ctx).finish();
                break;
        }
    }
}
