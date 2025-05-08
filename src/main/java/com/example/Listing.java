package com.example;

public class Listing {
    private String image;
    private String title;
    private String description;
    private String price;
    private String category;
    private String date;
    private String location;

    // Constructor
    public Listing(String image, String title, String description, String price, 
                   String category, String date, String location) {
        this.image = image;
        this.title = title;
        this.description = description;
        this.price = price;
        this.category = category;
        this.date = date;
        this.location = location;
    }

    // Getter and Setter methods for all fields
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
