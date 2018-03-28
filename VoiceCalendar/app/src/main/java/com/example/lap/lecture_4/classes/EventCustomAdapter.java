package com.example.lap.lecture_4.classes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lap.lecture_4.R;

import java.util.ArrayList;

/**
 * Created by lap on 3/23/2018.
 */

public class EventCustomAdapter extends ArrayAdapter<Event> {

    public static class ViewHolder{
        TextView lbl_eventName;
        TextView lbl_eventDetails;
    }

    public EventCustomAdapter(@NonNull Context context, ArrayList<Event> eventList) {
        super(context, R.layout.event_item, eventList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder vh;
        Event event = getItem(position);
        if(convertView == null)
        {
            vh = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.event_item, parent, false);
            vh.lbl_eventName = (TextView)convertView.findViewById(R.id.lbl_eventName);
            vh.lbl_eventDetails = (TextView)convertView.findViewById(R.id.lbl_eventDetails);
            convertView.setTag(vh);
        }
        else {
            vh= (ViewHolder) convertView.getTag();
        }
        vh.lbl_eventName.setText(event.getEventName());
        vh.lbl_eventDetails.setText(event.getEventDetails());

        return convertView;

    }
}
