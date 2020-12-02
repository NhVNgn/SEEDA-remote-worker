package com.sereem.remoteworker.model;

public class Message {
    private String userId;
    private String text;
    private String timeStamp;

    public Message(String userId, String text, String timeStamp) {
        this.userId = userId;
        this.text = text;
        this.timeStamp = timeStamp;
    }

    public Message() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
