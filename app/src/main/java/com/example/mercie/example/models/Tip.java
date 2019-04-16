package com.example.mercie.example.models;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Tip implements Serializable {

    @Exclude
    private String id;

    private String tipPackage;
    private String description;
    private String remedy;
    private String diet;
    private String tipImageName;

    public Tip() {
    }

    public Tip(String tipPackage, String description, String remedy, String diet, String tipImageName) {
        this.tipPackage = tipPackage;
        this.description = description;
        this.remedy = remedy;
        this.diet = diet;
        this.tipImageName = tipImageName;
    }

    public String getTipPackage() {
        return tipPackage;
    }

    public String getDescription() {
        return description;
    }

    public String getRemedy() {
        return remedy;
    }

    public String getDiet() {
        return diet;
    }

    public String getTipImageName() {
        return tipImageName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
