package com.sereem.remoteworker.model.siteAttendance;

import java.util.ArrayList;
import java.util.List;

/**
 * attendanceConstants class, not used by the application, used in iteration 2 to support
 * attendance database. Holds constants for attendance database.
 */
public class attendanceConstants {
    public static final String DATABASE_NAME = "attendanceDatabase";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "ATTENDANCEDATABASE";
    public static final String SITE_ID = "siteID";
    public static final String WORKER_EMAIL = "worker_email";
    public static final String UID = "_id";

    public List<String> arrayOfConst;

    public attendanceConstants() {
        arrayOfConst = new ArrayList<>();
        arrayOfConst.add(WORKER_EMAIL);
        arrayOfConst.add(SITE_ID);
    }
}
