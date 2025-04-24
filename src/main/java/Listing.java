package com.example;

public class Listing {
    private String title;
    private String description;
    private double price;

    // Constructor
    public Listing(String title, String description, double price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    // Getters and setters
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Optional: Override toString for debugging or UI lists
    @Override
    public String toString() {
        return title + " - $" + price;
    }
}

