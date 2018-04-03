package com.example.lap.pedometer.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.lap.pedometer.R;
import com.example.lap.pedometer.classes.BaseActivity;
import com.example.lap.pedometer.classes.DatabaseAdapter;
import com.example.lap.pedometer.classes.RunRecord;
import com.example.lap.pedometer.classes.RunRecordsAdapter;

import java.util.ArrayList;

public class HistoryActivity extends BaseActivity {
    private ListView records_listView;
    private DatabaseAdapter databaseAdapter;
    private ArrayList<RunRecord> records;
    private RunRecordsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        databaseAdapter = new DatabaseAdapter(this);
        databaseAdapter.open();

        records = databaseAdapter.getRunRecordsofUser(SplashActivity.getCurrentUser().getId());
        adapter = new RunRecordsAdapter(this,records);
        records_listView = (ListView)findViewById(R.id.records_listView);

        records_listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseAdapter.close();
    }
}
