package com.example.mercie.example.models;

import android.support.annotation.NonNull;

public class SalonService {

    private String packageName;
    private String serviceName;
    private String serviceCost;

    public SalonService() {
    }

    public SalonService(String packageName, String serviceName, String serviceCost) {
        this.packageName = packageName;
        this.serviceName = serviceName;
        this.serviceCost = serviceCost;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceCost() {
        return serviceCost;
    }

    public void setServiceCost(String serviceCost) {
        this.serviceCost = serviceCost;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("%s : %s(%s)", this.packageName, this.serviceName, this.serviceCost);
    }
}
