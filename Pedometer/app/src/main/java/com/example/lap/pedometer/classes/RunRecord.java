package com.example.lap.pedometer.classes;

/**
 * Created by lap on 3/30/2018.
 */

/*
The Pedometer application counts the users’ steps when they walk or run. It lets users know the
number of calories they burn, distance walked, walking time and speed per hour.
and saves them in database
 */
public class RunRecord {
    private int id, userId;
    private float numCalories, distance, speed, duration;
    private String time;
    private int numSteps;

    public RunRecord()
    {
        this.id = 0;
        this.userId = 0;
        numSteps = 0;
        numCalories = 0;
        distance = 0;
        time = "";
        speed = 0;
        duration = 0;
    }
    public float getDistance() {
        return distance;
    }

    public float getNumCalories() {
        return numCalories;
    }

    public int getNumSteps() {
        return numSteps;
    }

    public String getTime() {
        return time;
    }

    public float getSpeed() {
        return speed;
    }

    public int getUserId() {
        return userId;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public void setNumCalories(float numCalories) {
        this.numCalories = numCalories;
    }

    public void setNumSteps(int numSteps) {
        this.numSteps = numSteps;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }
}
