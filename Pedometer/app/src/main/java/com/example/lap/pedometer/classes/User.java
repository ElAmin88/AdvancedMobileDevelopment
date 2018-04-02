package com.example.lap.pedometer.classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by lap on 3/30/2018.
 */

public class User {
    private int id;
    private String name, password;
    private float weight, stepLegth, height;
    private byte[] picture;

    public User()
    {
        this.id = 0;
        this.name = "";
        this.password = "";
        this.weight = 1;
        this.stepLegth = 1;
        this.height = 1;
        this.picture = null;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public float getStepLegth() {
        return stepLegth;
    }

    public float getWeight() {
        return weight;
    }

    public void setStepLegth(float stepLegth) {
        this.stepLegth = stepLegth;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}
