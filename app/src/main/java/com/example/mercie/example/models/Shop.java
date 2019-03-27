package com.example.mercie.example.models;

import java.io.Serializable;

public class Shop implements Serializable {

    private String officialName;
    private String location;
    private String mobile;
    private String website;
    private String prifileImageName;
    private String openFrom;
    private String openTo;

    public Shop() {
    }

    public Shop(String officialName, String location, String mobile, String website, String prifileImageName, String openFrom, String openTo) {
        this.officialName = officialName;
        this.location = location;
        this.mobile = mobile;
        this.website = website;
        this.prifileImageName = prifileImageName;
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

    public String getPrifileImageName() {
        return prifileImageName;
    }

    public String getOpenFrom() {
        return openFrom;
    }

    public String getOpenTo() {
        return openTo;
    }
}
