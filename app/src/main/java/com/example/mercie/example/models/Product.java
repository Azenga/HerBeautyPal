package com.example.mercie.example.models;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Product implements Serializable {

    @Exclude
    private String id;
    private String packageName;
    private String name;
    private double cost;
    private String imageName;

    public Product() {
    }

    public Product(String packageName, String name, double cost, String imageName) {
        this.packageName = packageName;
        this.name = name;
        this.cost = cost;
        this.imageName = imageName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
