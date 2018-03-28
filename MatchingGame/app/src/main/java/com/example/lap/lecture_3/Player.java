package com.example.lap.lecture_3;

/**
 * Created by lap on 3/6/2018.
 */

public class Player {
    private String username, date;
    private int score;
    public Player(String name, int s ,String d)
    {
        username = name;
        score = s;
        date = d;


    }

    public String getUsername(){
        return username;
    }

    public String getDate(){
        return date;
    }

    public int getScore(){
        return score;
    }
}
