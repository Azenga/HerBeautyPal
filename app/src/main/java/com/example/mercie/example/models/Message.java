package com.example.mercie.example.models;

import com.google.firebase.firestore.Exclude;

import java.io.Serializable;

public class Message implements Serializable {

    @Exclude
    private String id;

    private String fromId;
    private String toId;
    private String content;

    public Message() {
    }

    public Message(String fromId, String toId, String content) {
        this.fromId = fromId;
        this.toId = toId;
        this.content = content;
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

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
