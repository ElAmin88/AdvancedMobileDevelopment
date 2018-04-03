package com.example.lap.pedometer.classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lap.pedometer.R;

import java.util.ArrayList;

/**
 * Created by lap on 4/3/2018.
 */

public class RunRecordsAdapter extends ArrayAdapter<RunRecord> {
    public RunRecordsAdapter(@NonNull Context context, ArrayList<RunRecord> records) {
        super(context, R.layout.list_item,records);
    }
    public static class ViewHolder{
        TextView lbl_time, lbl_distance, lbl_duration
                , lbl_speed, lbl_calories, lbl_numsteps ,lbl_number;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder vh;
        RunRecord record = getItem(position);
        if(convertView == null)
        {
            vh = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            vh.lbl_distance = (TextView)convertView.findViewById(R.id.lbl_distance);
            vh.lbl_calories = (TextView)convertView.findViewById(R.id.lbl_calories);
            vh.lbl_duration = (TextView)convertView.findViewById(R.id.lbl_duration);
            vh.lbl_time = (TextView)convertView.findViewById(R.id.lbl_time);
            vh.lbl_speed = (TextView)convertView.findViewById(R.id.lbl_speed);
            vh.lbl_numsteps = (TextView)convertView.findViewById(R.id.lbl_numSteps);
            vh.lbl_number = (TextView)convertView.findViewById(R.id.lbl_number);
            convertView.setTag(vh);
        }
        else {
            vh= (ViewHolder) convertView.getTag();
        }
        vh.lbl_number.setText((position+1)+"");
        int duration = (int)record.getDuration();
        int m = (int)(duration/60);
        int h = m/60;
        if(m<10)
            vh.lbl_duration.setText("Duration: " + h + ":0" + m + "," + duration%60);
        else
            vh.lbl_duration.setText("Duration: " + h + ":" + m + "," + duration%60);
        vh.lbl_calories.setText("Calories: "+record.getNumCalories());
        float distance = record.getDistance();
        float meter = distance/100;
        float km = m/1000;
        if (meter < 1000)
            vh.lbl_distance.setText("Distance: "+meter +" Meters");
        else
            vh.lbl_distance.setText("Distance: "+ km +" KM");
        vh.lbl_time.setText(record.getTime());
        vh.lbl_speed.setText("Avg. speed: "+record.getSpeed());
        vh.lbl_numsteps.setText("Total steps: "+record.getNumSteps());


        return convertView;

    }
}