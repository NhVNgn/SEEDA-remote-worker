package com.sereem.remoteworker.model.workSite;

import java.util.ArrayList;
import java.util.List;

/**
 * SiteConstants class, not used by the application, used in iteration 2 to support
 * worksite database. Holds constants for worksite database.
 */
public class SiteConstants {
    public static final String DATABASE_NAME = "siteDatabase";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "SITEDATABASE";
    public static final String SITE_ID = "siteID";
    public static final String NAME = "name";
    public static final String LOCATION = "location";
    public static final String HOURS = "HOURS";
    public static final String UID = "_id";
    public static final String MASTER_POINT = "masterPoint";

    public List<String> arrayOfConst;

    public SiteConstants() {
        arrayOfConst = new ArrayList<>();
        arrayOfConst.add(NAME);
        arrayOfConst.add(SITE_ID);
        arrayOfConst.add(LOCATION);
        arrayOfConst.add(HOURS);
        arrayOfConst.add(MASTER_POINT);
    }

}
