package com.example.mercie.example.models;

import java.io.Serializable;

public class Salonist implements Serializable {

    private String name;
    private String location;
    private String contact;
    private String website;
    private String openFrom;
    private String openTo;
    private String profilePicName;

    public Salonist() {
    }

    public Salonist(String name, String location, String contact, String website, String openFrom, String openTo, String profilePicName) {
        this.name = name;
        this.location = location;
        this.contact = contact;
        this.website = website;
        this.openFrom = openFrom;
        this.openTo = openTo;
        this.profilePicName = profilePicName;
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

    public String getProfilePicName() {
        return profilePicName;
    }

    public void setProfilePicName(String profilePicName) {
        this.profilePicName = profilePicName;
    }

    @Override
    public String toString() {
        return String.format("%s salon added", getName());
    }
}
