package com.example.mercie.example.models;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class SalonistNotification implements Serializable {

    @Exclude
    String id;

    private String type;
    private String serviceName;
    private String salonName;
    private String requestedTime;
    private String requestedDate;
    private Timestamp timestamp;
    private String clientId;

    public SalonistNotification() {
    }

    public SalonistNotification(String type, String serviceName, String salonName, String requestedTime, String requestedDate, Timestamp timestamp, String clientId) {
        this.type = type;
        this.serviceName = serviceName;
        this.salonName = salonName;
        this.requestedTime = requestedTime;
        this.requestedDate = requestedDate;
        this.timestamp = timestamp;
        this.clientId = clientId;
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

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getRequestedTime() {
        return requestedTime;
    }

    public void setRequestedTime(String requestedTime) {
        this.requestedTime = requestedTime;
    }

    public String getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(String requestedDate) {
        this.requestedDate = requestedDate;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSalonName() {
        return salonName;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }
}
