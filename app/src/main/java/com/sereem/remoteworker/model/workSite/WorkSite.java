package com.sereem.remoteworker.model.workSite;

import java.util.List;

/**
 * WorkSite class, used for holding information about a single worksite.
 */
public class WorkSite {
    private String name;
    private String siteID;
    private String location;
    private String hours;
    private String masterPoint;
    private List<Object> workers;
    private static WorkSite chosenWorksite;

    public WorkSite(String name, String siteID, String location, String hours, String masterPoint) {
        this.name = name;
        this.siteID = siteID;
        this.location = location;
        this.hours = hours;
        this.masterPoint = masterPoint;
    }

    public static void setChosenWorksite(WorkSite worksite) {
        chosenWorksite = worksite;
    }

    public static WorkSite getChosenWorksite() {
        return chosenWorksite;
    }

    public WorkSite() {

    }

    public List<Object> getWorkers() {
        return workers;
    }

    public void setWorkers(List<Object> workers) {
        this.workers = workers;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
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

    public String getSiteID() {
        return siteID;
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
