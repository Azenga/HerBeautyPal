package com.example.mercie.example.models;

import java.io.Serializable;

public class PShop implements Serializable {
    private String id;
    private String coverName;
    private String name;
    private String location;

    public PShop(String id, String coverName, String name, String location) {
        this.id = id;
        this.coverName = coverName;
        this.name = name;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getCoverName() {
        return coverName;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }
}
