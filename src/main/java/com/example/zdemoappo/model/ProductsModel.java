package com.example.zdemoappo.model;

public class    ProductsModel {

    String title, imageUrl, description, category;

    public ProductsModel(String title, String imageUrl, String description, String category) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
        this.category = category;
    }

    public ProductsModel(String title, String description, String category) {
        this.title = title;
        this.description = description;
        this.category = category;
    }

    public ProductsModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
