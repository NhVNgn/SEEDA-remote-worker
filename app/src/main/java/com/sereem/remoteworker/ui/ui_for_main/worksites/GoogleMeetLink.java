package com.sereem.remoteworker.ui.ui_for_main.worksites;

/**
 * GoogleMeetLink class, used for storing information about a single google meet link.
 */

public class GoogleMeetLink {
    private String userId;
    private String link;
    private String timeStamp;
    private String host;
    private String siteId;

    public GoogleMeetLink(String userId, String link, String timeStamp, String host, String siteId) {
        this.userId = userId;
        this.link = link;
        this.timeStamp = timeStamp;
        this.host = host;
        this.siteId = siteId;
    }

    public GoogleMeetLink(){

    }

    public String getSiteId() {
        return siteId;
    }

    public String getHost(){
        return host;
    }

    public String getUserId() {
        return userId;
    }

    public String getLink() {
        return link;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
