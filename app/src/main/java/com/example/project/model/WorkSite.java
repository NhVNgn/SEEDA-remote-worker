package com.example.project.model;

import java.util.List;

public class WorkSite {
    private String name;
    private int UID;
    private String siteId;
    private String location;
    private String hours;
    private List<User> workerList;
    private List<User> contactList;

    public void setName(String name) {
        this.name = name;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }
}
