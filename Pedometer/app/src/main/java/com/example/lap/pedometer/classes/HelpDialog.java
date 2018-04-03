package com.example.lap.pedometer.classes;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;
import android.widget.Button;

import com.example.lap.pedometer.R;


/**
 * Created by lap on 3/25/2018.
 */

public class HelpDialog extends Dialog {
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

    }


}
