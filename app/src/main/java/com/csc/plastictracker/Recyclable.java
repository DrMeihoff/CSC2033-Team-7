package com.csc.plastictracker;

import java.util.Calendar;
import java.util.Date;

public class Recyclable {
    private String barcodeId;
    private float weight;
    private String name;
    private String description;
    private Date date;

    public Recyclable(){}

    public Recyclable(String barcodeId, float weight, String name, String description) {
        this.barcodeId = barcodeId;
        this.weight = weight;
        this.name = name;
        this.description = description;
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, Calendar.getInstance().get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, Calendar.getInstance().get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        this.date = calendar.getTime();
    }

    public String getBarcodeId() {
        return barcodeId;
    }

    public void setBarcodeId(String barcodeId) {
        this.barcodeId = barcodeId;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
