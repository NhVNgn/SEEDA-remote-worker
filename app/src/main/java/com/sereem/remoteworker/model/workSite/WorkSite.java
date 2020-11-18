package com.sereem.remoteworker.model.workSite;

import com.sereem.remoteworker.model.User;

import java.util.List;

public class WorkSite {
    private String name;
    private int UID;
    private String siteId;
    private String location;
    private String hours;
    private String masterPoint;
    private List<User> workerList;
    private List<User> contactList;

    public WorkSite(String name, String siteId, String location, String hours, String masterPoint) {
        this.name = name;
        this.siteId = siteId;
        this.location = location;
        this.hours = hours;
        this.masterPoint = masterPoint;
    }

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

    public String getName() {
        return name;
    }

    public String getSiteId() {
        return siteId;
    }

    public String getLocation() {
        return location;
    }

    public String getHours() {
        return hours;
    }

    public void setMasterPoint(String masterPoint) {
        this.masterPoint = masterPoint;
    }

    public String getMasterPoint() {
        return masterPoint;
    }

    @Override
    public String toString() {
        return name;
    }
}
