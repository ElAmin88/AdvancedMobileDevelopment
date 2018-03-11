package com.example.lap.lecture_3;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class ScoreCustomAdapter extends ArrayAdapter<Player> {
    public static class ViewHolder{
        TextView lbl_username;
        TextView lbl_score;
        TextView lbl_date;
    }
    public ScoreCustomAdapter(Context context , ArrayList<Player> playerList)
    {
        super(context, R.layout.item, playerList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder vh;
        Player p = getItem(position);
        if(convertView == null)
        {
            vh = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item, parent, false);
            vh.lbl_date = (TextView)convertView.findViewById(R.id.lbl_date);
            vh.lbl_username = (TextView)convertView.findViewById(R.id.lbl_username);
            vh.lbl_score = (TextView)convertView.findViewById(R.id.lbl_score);
            convertView.setTag(vh);
        }
        else {
            vh= (ViewHolder) convertView.getTag();
        }
        vh.lbl_score.setText(p.getScore()+"");
        vh.lbl_username.setText(p.getUsername());
        vh.lbl_date.setText(p.getDate());

        return convertView;

    }
}
