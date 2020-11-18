package com.sereem.remoteworker.model.siteAttendance;

public class Attendance {
    private String workerEmail;
    private int UID;
    private String siteID;

    public void setWorkerEmail(String workerEmail) {
        this.workerEmail = workerEmail;
    }

    public Attendance(String workerEmail, String siteID) {
        this.workerEmail = workerEmail;
        this.siteID = siteID;
    }

    public void setUID(int UID) {
        this.UID = UID;
    }

    public void setSiteID(String siteID) {
        this.siteID = siteID;
    }

    public String getWorkerEmail() {
        return workerEmail;
    }

    public String getSiteID() {
        return siteID;
    }
}
