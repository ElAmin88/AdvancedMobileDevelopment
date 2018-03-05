package com.example.lap.lecture_3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    ArrayList<String> initial_list, current_list;
    ImageButton btn_0,btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7;
    Random r;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        init();

    }

    private void init()
    {
        btn_0 =(ImageButton)findViewById(R.id.btn_0);
        btn_1 =(ImageButton)findViewById(R.id.btn_1);
        btn_2 =(ImageButton)findViewById(R.id.btn_2);
        btn_3 =(ImageButton)findViewById(R.id.btn_3);
        btn_4 =(ImageButton)findViewById(R.id.btn_4);
        btn_5 =(ImageButton)findViewById(R.id.btn_5);
        btn_6 =(ImageButton)findViewById(R.id.btn_6);
        btn_7 =(ImageButton)findViewById(R.id.btn_7);

        initial_list = new ArrayList<String>();
        current_list = new ArrayList<String>();

        r = new Random();

        initial_list.add("Mouse");
        initial_list.add("Dog");
        initial_list.add("Cat");
        initial_list.add("Rabbit");
        initial_list.add("Dog");
        initial_list.add("Mouse");
        initial_list.add("Rabbit");
        initial_list.add("Cat");
        int x;
        for (int i=0;i<8;i++)
        {
            if(initial_list.size()>1)
            {
                x = r.nextInt(initial_list.size());
                current_list.add(initial_list.get(x));
                initial_list.remove(x);
            }
            else
            {
                current_list.add(initial_list.get(0));
                initial_list.remove(0);
            }
        }
    }
}
