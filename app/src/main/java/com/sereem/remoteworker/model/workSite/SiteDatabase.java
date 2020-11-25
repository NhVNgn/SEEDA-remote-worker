package com.sereem.remoteworker.model.workSite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class    SiteDatabase {
    private SQLiteDatabase db;
    private Context context;
    private List<WorkSite> allWorkSite;
    private final dbSiteHelper helper;
    SiteConstants constants = new SiteConstants();
    public SiteDatabase (Context c){
        context = c;
        helper = new dbSiteHelper(context);
        allWorkSite = new ArrayList<>();
        // add sample to siteDatabase
        addSample("Burnaby construction site", "burnaby123", "8888 University Dr, Burnaby", "08:00AM-05:00PM" , "30");
        addSample("Vancouver construction site", "van123", "515 W Hasting St, Vancouver", "11:30AM-05:00PM", "25");
        addSample("Surrey construction site", "surrey123", "10153 King George Blvd, Surrey", "08:30AM-05:00PM", "40");
        addSample("Coquitlam construction site", "coquitlam123", "2929 Barnet Hwy, Coquitlam", "12:30AM-05:00PM", "10");
        addSample("Port Moody construction site", "portmoody123", "300 loco Rd, Port Moody", "07:30AM-05:00PM", "15");
    }

    public List<WorkSite> getAllWorkSite() {
        return allWorkSite;
    }

    public long insertData (List<String> args){
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < args.size(); i++){
            contentValues.put(constants.arrayOfConst.get(i), args.get(i));
        }
        long id = db.insert(SiteConstants.TABLE_NAME, null, contentValues);
        return id;
    }

    public void addSample(String name, String siteID, String location, String hours, String masterPoint){
        // search if siteID is already in the table
        if (getSite(siteID) != null)
            return;


        List<String> argsArray = new ArrayList<>();
        argsArray.add(name);
        argsArray.add(siteID);
        argsArray.add(location);
        argsArray.add(hours);
        argsArray.add(masterPoint);
        long id = insertData(argsArray);

        if (id < 0)
        {
            System.out.println("fail to add site");
        }
        else {
            allWorkSite.add(new WorkSite(name, siteID, location, hours, masterPoint));
            System.out.println("site added successfully");
        }
    }

    public WorkSite getSite(String siteID){
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM " + SiteConstants.TABLE_NAME +
                " WHERE " + SiteConstants.SITE_ID + " =?";
        Cursor cursor = db.rawQuery(sql, new String[] {siteID});
        WorkSite ws = new WorkSite("", "", "", "", "");

        if (cursor.moveToFirst()){
            if (cursor.getString(cursor.getColumnIndex(SiteConstants.SITE_ID)).equals(siteID)) {
                populateWorkSite(ws, cursor);
                allWorkSite.add(ws);
            }

            return ws;
        }
        else
            return null;
    }
    public void populateWorkSite(WorkSite ws, Cursor cursor){
//        ws.setUID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(SiteConstants.UID))));
        ws.setSiteID(cursor.getString(cursor.getColumnIndex(SiteConstants.SITE_ID)));
        ws.setName(cursor.getString(cursor.getColumnIndex(SiteConstants.NAME)));
        ws.setHours(cursor.getString(cursor.getColumnIndex(SiteConstants.HOURS)));
        ws.setLocation(cursor.getString(cursor.getColumnIndex(SiteConstants.LOCATION)));
        ws.setMasterPoint(cursor.getString(cursor.getColumnIndex(SiteConstants.MASTER_POINT)));
    }
}



