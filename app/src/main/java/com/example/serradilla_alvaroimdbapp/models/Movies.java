package com.example.serradilla_alvaroimdbapp.models;

public class Movies {
    private String id;
    private String name;
    private String imageUrl;

    public Movies(String id, String name, String imageUrl) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}






