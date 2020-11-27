package com.sereem.remoteworker.model;


public class UserLocation {
    public String timeStamp;
    public String location;

    public UserLocation(String location, String timeStamp) {
        this.location = location;
        this.timeStamp = timeStamp;
    }

    public UserLocation() {
    }
}
