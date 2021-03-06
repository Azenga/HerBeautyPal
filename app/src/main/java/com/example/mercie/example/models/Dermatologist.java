package com.example.mercie.example.models;

import java.io.Serializable;

public class Dermatologist implements Serializable {

    private String officialName;
    private String location;
    private String mobile;
    private String website;
    private String openFrom;
    private String openTo;
    private String profilePicName;

    public Dermatologist() {
    }

    public Dermatologist(String officialName, String location, String mobile, String website, String openFrom, String openTo, String profilePicName) {
        this.officialName = officialName;
        this.location = location;
        this.mobile = mobile;
        this.website = website;
        this.openFrom = openFrom;
        this.openTo = openTo;
        this.profilePicName = profilePicName;
    }

    public String getOfficialName() {
        return officialName;
    }

    public String getLocation() {
        return location;
    }

    public String getMobile() {
        return mobile;
    }

    public String getWebsite() {
        return website;
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
}
