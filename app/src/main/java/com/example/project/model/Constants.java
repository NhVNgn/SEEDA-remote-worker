package com.example.project.model;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final String DATABASE_NAME = "workerDatabase";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "WORKERDATABASE";
    public static final String COMPANY_ID = "CompanyId";
    public static final String FIRST_NAME = "FirstName";
    public static final String LAST_NAME = "LastName";
    public static final String EMAIL = "Email";
    public static final String PASSWORD = "Password";
    public static final String PHONE = "phone";
    public static final String BIRTHDAY = "birthDay";
    public static final String EM_FIRST_NAME = "emergencyFirstName";
    public static final String EM_LAST_NAME = "emergencyLastName";
    public static final String EM_PHONE = "emergencyPhone";
    public static final String EM_RELATION = "emergencyRelation";
    public static final String MED_CONSIDERATIONS = "MedicalConsideration";
    public static final String ICON_RES = "iconRes";
    public static final String UID = "_id";

    public List<String> arrayOfConst;

    public Constants() {
        arrayOfConst = new ArrayList<>();
        arrayOfConst.add(FIRST_NAME);
        arrayOfConst.add(LAST_NAME);
        arrayOfConst.add(EMAIL);
        arrayOfConst.add(PASSWORD);
        arrayOfConst.add(PHONE);
        arrayOfConst.add(BIRTHDAY);
        arrayOfConst.add(COMPANY_ID);
        arrayOfConst.add(EM_FIRST_NAME);
        arrayOfConst.add(EM_LAST_NAME);
        arrayOfConst.add(EM_PHONE);
        arrayOfConst.add(EM_RELATION);
        arrayOfConst.add(MED_CONSIDERATIONS);
        arrayOfConst.add(ICON_RES);
    }


}
