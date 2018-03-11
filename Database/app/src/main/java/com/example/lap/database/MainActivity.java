package com.example.lap.database;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText username, score, date;
    Button btn_Add, btn_Display;
    DatabaseAdapter DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText)findViewById(R.id.editText_Username);
        score = (EditText)findViewById(R.id.editText_Score);
        date = (EditText)findViewById(R.id.editText_Date);
        btn_Add = (Button)findViewById(R.id.btn_Add);
        btn_Display = (Button)findViewById(R.id.btn_Display);
        DB = new DatabaseAdapter(this);
        DB.open();

        btn_Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = username.getText().toString();
                int s;
                try{
                    s = Integer.parseInt(score.getText().toString());
                }catch (Exception e){
                    s = 0;
                }
                String d = date.getText().toString();
                long i=0;
                if(name !="" && s !=0 &&d !="") {
                    i = DB.insertRow(name, s, d);
                    Toast.makeText(getBaseContext(),"Add Record "+i,Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getBaseContext(),"No Record add Please Enter values"+i,Toast.LENGTH_LONG).show();
            }
        });

        btn_Display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor cr = DB.getAllRows();
                String message ="";
                if (cr.moveToFirst())
                {
                    do{

                        // Process the data:
                        int id = cr.getInt(DB.COL_ROWID);
                        String name = cr.getString(DB.COL_USERNAME);
                        int sc = cr.getInt(DB.COL_SCORE);
                        String dt = cr.getString(DB.COL_DATE);

                        // Append data to the message:
                        message = "id=" + id
                                +", name=" + name
                                +", #=" + sc
                                +", Colour=" + dt
                                +"\n";
                        Log.d("Data",message);
                        Log.d("Data","=============");
                        // [TO_DO_B6]
                        // Create arraylist(s)? and use it(them) in the list view
                    }while (cr.moveToNext());
                }
                Toast.makeText(getBaseContext(),"Display Record",Toast.LENGTH_LONG).show();
            }

        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DB.close();
    }
}
