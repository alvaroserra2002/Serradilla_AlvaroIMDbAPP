package com.example.serradilla_alvaroimdbapp.models;

import java.util.Date;

public class Movies {
    private String name;
    private String text;
    private double raiting;
    private Date realising;

    public Movies(String name, String text, double raiting, Date realising) {
        this.name = name;
        this.text = text;
        this.raiting = raiting;
        this.realising = realising;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getRaiting() {
        return raiting;
    }

    public void setRaiting(double raiting) {
        this.raiting = raiting;
    }

    public Date getRealising() {
        return realising;
    }

    public void setRealising(Date realising) {
        this.realising = realising;
    }
}
