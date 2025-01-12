package com.example.serradilla_alvaroimdbapp.models;

public class Movies {
    private String id;
    private String name;
    private float rating;
    private String imageUrl;
    private String plot;

    public Movies(String id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public Movies(String id, String name, float rating, String imageUrl, String plot) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.imageUrl = imageUrl;
        this.plot = plot;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getRating(){
        return rating;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPlot() {
        return plot;
    }
}






