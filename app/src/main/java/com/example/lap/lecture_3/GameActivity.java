package com.example.lap.lecture_3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    ArrayList<String> initial_list, current_list;
    ArrayList<ImageButton> buttons;
    ArrayList<Integer> removed_buttons;
    ImageButton btn_0,btn_1,btn_2,btn_3,btn_4,btn_5,btn_6,btn_7;
    Random r;
    String clicked ="";
    int clickedId = 1000, time=0;
    LinearLayout layout;
    TextView txt_timer;
    Timer timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        if(savedInstanceState == null)
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

        layout =(LinearLayout)findViewById(R.id.parentLayout);
        txt_timer =(TextView)findViewById(R.id.txt_Timer);
        txt_timer.setText(time+"");
        initial_list = new ArrayList<String>();
        current_list = new ArrayList<String>();
        buttons = new ArrayList<ImageButton>();
        removed_buttons = new ArrayList<Integer>();

        r = new Random();

        buttons.add(btn_0);
        buttons.add(btn_1);
        buttons.add(btn_2);
        buttons.add(btn_3);
        buttons.add(btn_4);
        buttons.add(btn_5);
        buttons.add(btn_6);
        buttons.add(btn_7);

        initial_list.add("Mouse");
        initial_list.add("Dog");
        initial_list.add("Cat");
        initial_list.add("Bird");
        initial_list.add("Dog");
        initial_list.add("Mouse");
        initial_list.add("Bird");
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

    public void btn_clicked(View v)
    {
        switch (v.getId())
        {
            case (R.id.btn_0):
                onClick(0);
                break;
            case (R.id.btn_1):
                onClick(1);
                break;
            case (R.id.btn_2):
                onClick(2);
                break;
            case (R.id.btn_3):
                onClick(3);
                break;
            case (R.id.btn_4):
                onClick(4);
                break;
            case (R.id.btn_5):
                onClick(5);
                break;
            case (R.id.btn_6):
                onClick(6);
                break;
            case (R.id.btn_7):
                onClick(7);
                break;
        }

    }

    private void onClick( int id)
    {
        setImage(id);
        if(clicked.equals(""))
        {
            clicked = current_list.get(id);
            clickedId = id;

        }
        else if(clicked.equals(current_list.get(id)) && clickedId !=id)
        {
            removed_buttons.add(id);
            removed_buttons.add(clickedId);
            ((ViewManager)buttons.get(id).getParent()).removeView(buttons.get(id));
            ((ViewManager)buttons.get(clickedId).getParent()).removeView(buttons.get(clickedId));
            clicked = "";
            clickedId = 1000;
            if(removed_buttons.size()>=8)
            {
                endGame();

            }
        }
        else
        {
            /*try{
                TimeUnit.SECONDS.sleep(5);
            }catch (Exception e){};
            resetImage(id);
            resetImage(clickedId);
            clicked = "";
            clickedId = 1000;*/
            resetImage(clickedId);
            clicked = current_list.get(id);
            clickedId = id;


        }
    }

    private void setImage(int id)
    {
        String img;
        if(id<8)
            img = current_list.get(id);
        else
            img ="";
        switch (img)
        {
            case ("Dog"):
                buttons.get(id).setImageResource(R.drawable.dog);
                break;
            case ("Cat"):
                buttons.get(id).setImageResource(R.drawable.cat);
                break;
            case ("Bird"):
                buttons.get(id).setImageResource(R.drawable.bird);
                break;
            case ("Mouse"):
                buttons.get(id).setImageResource(R.drawable.mouse);
                break;

        }
    }

    private void resetImage(int id)
    {
        buttons.get(id).setImageResource(R.drawable.mark);
    }

    private void startGame()
    {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                time++;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        txt_timer.setText(time+"");
                    }
                });
            }
        },0,1000);




    }

    private void endGame()
    {
        timer.cancel();
        Intent i = new Intent(getApplicationContext(),ScoreActivity.class);
        i.putExtra("score",time+"");
        startActivity(i);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        init();
        current_list = (ArrayList<String>)savedInstanceState.getStringArrayList("current");
        removed_buttons =(ArrayList<Integer>) savedInstanceState.getIntegerArrayList("deleted");
        for(int i =0;i<removed_buttons.size();i++)
        {
            int x = removed_buttons.get(i);
            ((ViewManager)buttons.get(x).getParent()).removeView(buttons.get(x));
        }
        time = savedInstanceState.getInt("time");
        clicked = (String)savedInstanceState.getString("clicked");
        clickedId = (Integer)savedInstanceState.getInt("clickedId");
        setImage(clickedId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putIntegerArrayList("deleted",removed_buttons);
        outState.putStringArrayList("current",current_list);
        outState.putString("clicked",clicked);
        outState.putInt("clickedId",clickedId);
        outState.putInt("time",time);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startGame();
    }
}

