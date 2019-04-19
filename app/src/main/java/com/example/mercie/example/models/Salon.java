package com.example.mercie.example.models;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Salon implements Serializable {

    @Exclude String id;

    private String name;
    private String location;
    private String contact;
    private String website;
    private String openFrom;
    private String openTo;
    private String coverImage;

    public Salon() {
    }

    public Salon(String name, String location, String contact, String website, String openFrom, String openTo) {
        this.name = name;
        this.location = location;
        this.website = website;
        this.openFrom = openFrom;
        this.openTo = openTo;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getOpenFrom() {
        return openFrom;
    }

    public void setOpenFrom(String openFrom) {
        this.openFrom = openFrom;
    }

    public String getOpenTo() {
        return openTo;
    }

    public void setOpenTo(String openTo) {
        this.openTo = openTo;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
