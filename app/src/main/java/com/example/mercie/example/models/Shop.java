package com.example.mercie.example.models;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Shop implements Serializable {

    @Exclude
    private String id;

    private String officialName;
    private String location;
    private String mobile;
    private String website;
    private String profileImageName;
    private String openFrom;
    private String openTo;

    public Shop() {
    }

    public Shop(String officialName, String location, String mobile, String website, String prifileImageName, String openFrom, String openTo) {
        this.officialName = officialName;
        this.location = location;
        this.mobile = mobile;
        this.website = website;
        this.profileImageName = prifileImageName;
        this.openFrom = openFrom;
        this.openTo = openTo;
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

    public String getProfileImageName() {
        return profileImageName;
    }

    public String getOpenFrom() {
        return openFrom;
    }

    public String getOpenTo() {
        return openTo;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setProfileImageName(String profileImageName) {
        this.profileImageName = profileImageName;
    }

    public void setOpenFrom(String openFrom) {
        this.openFrom = openFrom;
    }

    public void setOpenTo(String openTo) {
        this.openTo = openTo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
