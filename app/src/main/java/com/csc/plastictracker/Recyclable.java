package com.csc.plastictracker;

public class Recyclable {
    private String barcodeId;
    private float weight;
    private String name;
    private String description;

    public Recyclable(String s){}

    public Recyclable(String barcodeId, float weight, String name, String description) {
        this.barcodeId = barcodeId;
        this.weight = weight;
        this.name = name;
        this.description = description;
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
}
