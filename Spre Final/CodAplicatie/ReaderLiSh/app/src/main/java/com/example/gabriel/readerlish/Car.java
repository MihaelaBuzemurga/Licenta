package com.example.gabriel.readerlish;

import android.graphics.Bitmap;

/**
 * Created by Gabriel on 19.01.2017.
 */

public class Car {
    private String make;
    private int year;
    private int iconID;
    private String condition;
    private Bitmap imagine;

    public Car(String make, int year, int iconID, String condition,Bitmap imagine) {
        super();
        this.make = make;
        this.year = year;
        this.iconID = iconID;
        this.condition = condition;
        this.imagine = imagine;
    }

    public String getMake() {
        return make;
    }
    public int getYear() {
        return year;
    }
    public int getIconID() {
        return iconID;
    }
    public String getCondition() {
        return condition;
    }

    public Bitmap getImagine() {
        return imagine;
    }

    public void setImagine(Bitmap imagine) {
        this.imagine = imagine;
    }
}
