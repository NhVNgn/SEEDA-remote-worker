package com.sereem.remoteworker.model;

/**
 * UserLocation class, used for storing information about user's current location
 */

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
