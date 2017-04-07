package com.therishka.androidlabweather.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * @author Rishad Mustafaev
 */
public class Wind implements Serializable {

    @SerializedName("speed")
    private double mSpeed;

    public int getSpeed() {
        return (int) mSpeed;
    }
}
