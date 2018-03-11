package com.example.lap.lecture_3;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ScoreActivity extends AppCompatActivity {
    DatabaseAdapter DB;
    ListView lstView;
    ScoreCustomAdapter adapter;
    ArrayList<Player> players;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        DB = new DatabaseAdapter(this);
        DB.open();
        players = GetAll();

        lstView = (ListView)findViewById(R.id.lstView);
        adapter = new ScoreCustomAdapter(this,players);
        lstView.setAdapter(adapter);

    }

    private ArrayList<Player> GetAll()
    {
        ArrayList<Player> players = new ArrayList<Player>();
        Cursor cr = DB.getAllRows();
        if (cr.moveToFirst())
        {
            do{

                // Process the data:
                int id = cr.getInt(DB.COL_ROWID);
                String name = cr.getString(DB.COL_USERNAME);
                int sc = cr.getInt(DB.COL_SCORE);
                String dt = cr.getString(DB.COL_DATE);

                players.add(new Player(name,sc,dt));
            }while (cr.moveToNext());
        }
        return players;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DB.close();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getBaseContext(),MainActivity.class);
        startActivity(i);
        finish();
    }

}
