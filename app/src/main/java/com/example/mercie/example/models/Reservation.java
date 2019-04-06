package com.example.mercie.example.models;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Reservation implements Serializable {

    @Exclude
    String id;

    private boolean agreedUpon;
    private String type;
    private String item;
    private String time;

    public Reservation() {
    }

    public Reservation(String type, String item, String time) {
        this.type = type;
        this.item = item;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isAgreedUpon() {
        return agreedUpon;
    }

    public void setAgreedUpon(boolean agreedUpon) {
        this.agreedUpon = agreedUpon;
    }
}
