package com.example.mercie.example.models;

import java.io.Serializable;

public class Salonist implements Serializable {

    private String name;
    private String location;
    private String contact;
    private String website;
    private String profilePicName;

    public Salonist() {
    }

    public Salonist(String name, String location, String contact, String website, String profilePicName) {
        this.name = name;
        this.location = location;
        this.contact = contact;
        this.website = website;
        this.profilePicName = profilePicName;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getWebsite() {
        return website;
    }

    public String getProfilePicName() {
        return profilePicName;
    }

    public String getContact() {
        return contact;
    }

    @Override
    public String toString() {
        return String.format("%s salon added", getName());
    }
}
