package com.example.mercie.example.models;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Notification implements Serializable {

    @Exclude
    String id;

    private String type;
    private String serviceName;
    private String requestTime;
    private String clientId;

    public Notification() {
    }

    public Notification(String type, String serviceName, String requestTime, String clientId) {
        this.type = type;
        this.serviceName = serviceName;
        this.requestTime = requestTime;
        this.clientId = clientId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
