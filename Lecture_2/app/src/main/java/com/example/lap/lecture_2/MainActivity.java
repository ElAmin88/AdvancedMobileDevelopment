package com.example.lap.lecture_2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Spinner spinner_From ,spinner_To;
    EditText txt_Value;
    TextView lbl_result;
    Button btn_Convert;
    float value,result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner_From =(Spinner)findViewById(R.id.spinner_From);
        spinner_To =(Spinner)findViewById(R.id.spinner_To);
        txt_Value =(EditText)findViewById(R.id.editText);
        lbl_result =(TextView)findViewById(R.id.lbl_Result);
        btn_Convert =(Button)findViewById(R.id.button);

        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Items,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner_From.setAdapter(adapter);
        spinner_To.setAdapter(adapter);


        btn_Convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String from,to,v;
                v =txt_Value.getText().toString();
                try{
                    value = Float.parseFloat(v);
                }catch (Exception e){}
                if(value==0.0)
                    Toast.makeText(getBaseContext(),"Enter a valid Value",Toast.LENGTH_LONG).show();
                else{
                    from = (String) spinner_From.getSelectedItem();
                    to = (String) spinner_To.getSelectedItem();
                    result = Convert(value,from,to);
                    lbl_result.setText(result+"");
                }


            }
        });


    }

    private float Convert(float v,String from, String to)
    {
        float result;
        if(from.equals("EGP") && to.equals("USD"))
            result= v/(float) 17.6;
        else if(from.equals("USD") && to.equals("EGP"))
            result= v*(float) 17.6;
        else {
            Toast.makeText(getBaseContext(), "Same Currency is chosen", Toast.LENGTH_LONG).show();
            result = v;
        }
        return result;
    }
}
