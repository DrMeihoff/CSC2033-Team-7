package com.csc.plastictracker;

import java.util.Calendar;

public class UserRecyclable {

    private String barcodeId;
    private String uid;
    private int year;
    private int month;
    private int weekOfYear;
    private int dayOfYear;

    public UserRecyclable(){}

    public UserRecyclable(String barcodeId, String uid) {
        this.barcodeId = barcodeId;
        this.uid = uid;
        Calendar calendar = Calendar.getInstance();
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH);
        this.weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR);
        this.dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
    }

    public String getBarcodeId() {
        return barcodeId;
    }

    public void setBarcodeId(String barcodeId) {
        this.barcodeId = barcodeId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(int weekOfYear) {
        this.weekOfYear = weekOfYear;
    }

    public int getDayOfYear() {
        return dayOfYear;
    }

    public void setDayOfYear(int dayOfYear) {
        this.dayOfYear = dayOfYear;
    }
}
