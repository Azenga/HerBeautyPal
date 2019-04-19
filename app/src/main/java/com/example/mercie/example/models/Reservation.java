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
    private String date;
    private String salonName;
    private String salonistId;

    public Reservation() {
    }

    public Reservation(String type, String item, String time, String date, String salonName, String salonistId) {
        this.type = type;
        this.item = item;
        this.time = time;
        this.date = date;
        this.salonName = salonName;
        this.salonistId = salonistId;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isAgreedUpon() {
        return agreedUpon;
    }

    public void setAgreedUpon(boolean agreedUpon) {
        this.agreedUpon = agreedUpon;
    }

    public String getSalonistId() {
        return salonistId;
    }

    public void setSalonistId(String salonistId) {
        this.salonistId = salonistId;
    }

    public String getSalonName() {
        return salonName;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }
}
