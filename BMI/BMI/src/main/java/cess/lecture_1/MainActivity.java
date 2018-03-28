package cess.lecture_1;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView txt_1,bmi ;
    private Button btn_1;
    boolean b= true;
    float h,w,r;
    EditText edit_weight,edit_height;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bmi = (TextView)findViewById(R.id.txt_bmi);
        txt_1 = (TextView)findViewById(R.id.txt_Welcome) ;
        btn_1 =  (Button)findViewById(R.id.btn_Change);
        edit_height=(EditText)findViewById(R.id.edit_Height);
        edit_weight=(EditText)findViewById(R.id.edit_Weight);

        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    h = Float.parseFloat(edit_height.getText().toString());
                    w = Float.parseFloat(edit_weight.getText().toString());
                    r=w/(h*h);
                    bmi.setText(""+r);
                }
                catch (Exception e){
                    bmi.setText("Please Enter Numbers larger than ZERO");
                }



            }
        });
    }
}
