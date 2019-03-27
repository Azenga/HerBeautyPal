package com.example.mercie.example.models;

import java.io.Serializable;

public class Salonist implements Serializable {

    private String salonId;
    private String name;
    private String location;
    private String contact;
    private String website;
    private String openFrom;
    private String openTo;
    private String profilePicName;

    public Salonist() {
        // TODO: 3/25/19 Add the salon ID as a field in the salons so that we can get the salon services with the object
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

    public String getLocation() {
        return location;
    }

    public String getWebsite() {
        return website;
    }

    public String getSalonId() {
        return salonId;
    }

    public String getContact() {
        return contact;
    }

    public String getOpenFrom() {
        return openFrom;
    }

    public String getOpenTo() {
        return openTo;
    }

    public String getProfilePicName() {
        return profilePicName;
    }

    @Override
    public String toString() {
        return String.format("%s salon added", getName());
    }
}
