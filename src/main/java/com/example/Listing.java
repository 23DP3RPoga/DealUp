package com.example;

public class Listing {
    private final String image;
    private final String title;
    private final String description;
    private final String price;
    private final String category;
    private final String date;
    private final String location;

    public Listing(String image, String title, String description, String price, String category, String date, String location) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.date = date;
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }
}
