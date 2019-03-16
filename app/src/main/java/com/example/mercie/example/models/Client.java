package com.example.mercie.example.models;

public class Client {

    private String name;
    private String contact;
    private String address;
    private String gender;
    private String imageUrl;

    public Client() {}

    public Client(String name, String contact, String address, String gender, String imageUrl) {
        this.name = name;
        this.contact = contact;
        this.address = address;
        this.gender = gender;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
