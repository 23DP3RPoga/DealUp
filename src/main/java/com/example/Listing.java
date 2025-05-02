
package com.example;

import javafx.beans.property.SimpleStringProperty;

public class Listing {
    private final SimpleStringProperty title, image, description, price, category,date, location;

    public Listing(String title, String image, String description, String price, String category,String date, String location) {
        this.title = new SimpleStringProperty(title);
        this.image = new SimpleStringProperty(image);
        this.description = new SimpleStringProperty(description);
        this.price = new SimpleStringProperty(price);
        this.category = new SimpleStringProperty(category);
        this.date = new SimpleStringProperty(date);
        this.location = new SimpleStringProperty(location);
    }

    public String getTitle() { return title.get(); }
    public String getImage() { return image.get(); }
    public String getDescription() { return description.get(); }
    public String getPrice() { return price.get(); }
    public String getCategory() { return category.get(); }
    public String getDate() { return date.get(); }
    public String getLocation() { return location.get(); }
}
