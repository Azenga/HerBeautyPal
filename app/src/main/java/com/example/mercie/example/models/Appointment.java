package com.example.mercie.example.models;


import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Appointment implements Serializable {

    @Exclude
    String id;

    String fromId;
    String serviceName;
    String date;
    String time;

    public Appointment() {
    }

    public Appointment(String fromId, String serviceName, String date, String time) {
        this.fromId = fromId;
        this.serviceName = serviceName;
        this.date = date;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
