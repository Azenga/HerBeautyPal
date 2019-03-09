package com.example.mercie.example;

public class UserModel {

    private String name;
    private String email;
    private String contact;
    private String address;
    private String gender;
    private String imageUrl;

    public UserModel() {}

    public UserModel(String name, String email, String contact, String address, String gender, String imageUrl) {
        this.name = name;
        this.email = email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
