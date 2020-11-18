package com.sereem.remoteworker.model.siteAttendance;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class attendanceDatabase {
    private SQLiteDatabase db;
    private Context context;
    private final dbAttendHelper helper;
    attendanceConstants constants = new attendanceConstants();
    List<Attendance> allAttendanceList;
    public attendanceDatabase (Context c){
        context = c;
        helper = new dbAttendHelper(context);
        allAttendanceList = new ArrayList<>();
        // add sample to siteDatabase
        //addSample("Burnaby construction site", "burnaby123", "888 University Dr, Burnaby", "08:00AM-05:00PM" );
        //addSample("Vancouver construction site", "van123", "515 W Hasting St, Vancouver", "11:30AM-05:00PM");
        //addSample("Surrey construction site", "surrey123", "10153 King George Blvd, Surrey", "08:30AM-05:00PM");
        //addSample("Coquitlam construction site", "coquitlam123", "2929 Barnet Hwy, Coquitlam", "12:30AM-05:00PM");
        //addSample("Port Moody construction site", "portmoody123", "300 loco Rd, Port Moody", "07:30AM-05:00PM");

        addSample("nhanvyn@sfu.ca", "burnaby123");
        addSample("nhanvyn@sfu.ca", "surrey123");
        addSample("nhanvyn@sfu.ca", "van123");
        addSample("nhanvyn@sfu.ca", "portmoody123");

        addSample("dovsiien@sfu.ca", "burnaby123");
        addSample("dovsiien@sfu.ca", "van123");
        addSample("dovsiien@sfu.ca", "coquitlam123");


        addSample("nhanvyhl1234@gmail.com", "burnaby123");
        addSample("nhanvyhl1234@gmail.com", "portmoody123");

    }


    public List<Attendance> getAllAttendanceList() {
        List<Attendance> allAttendances = new ArrayList<>();
        db = helper.getReadableDatabase();
        String sql = "SELECT * FROM " + attendanceConstants.TABLE_NAME;
        Cursor cursor = db.rawQuery(sql,null);

        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            Attendance attendance = new Attendance("", "");
            populateAttendance(attendance, cursor);
            allAttendances.add(attendance);
            cursor.moveToNext();
        }

        cursor.close();
        return allAttendances;
    }

    public long insertData (List<String> args){
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < args.size(); i++){
            contentValues.put(constants.arrayOfConst.get(i), args.get(i));
        }
        long id = db.insert(attendanceConstants.TABLE_NAME, null, contentValues);
        return id;
    }

    public void addSample(String workerEmail, String siteID){
        // search if siteID is already in the table
        if (getAttendance(siteID, workerEmail) != null)
        {
            return;
        }


        List<String> argsArray = new ArrayList<>();
        argsArray.add(workerEmail);
        argsArray.add(siteID);
        long id = insertData(argsArray);
        if (id < 0)
        {
            System.out.println("fail to add attendance");
        }
        else {
            allAttendanceList.add(new Attendance(workerEmail, siteID));

        }
    }

    public Attendance getAttendance(String siteID, String workerEmail){
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM " + attendanceConstants.TABLE_NAME +
                " WHERE " + attendanceConstants.WORKER_EMAIL + " =?" + " AND " + attendanceConstants.SITE_ID + " =?";
        Cursor cursor = db.rawQuery(sql, new String[] {workerEmail, siteID});


        Attendance attendance = new Attendance("", "");
        if (cursor.moveToFirst()){
            if (cursor.getString(cursor.getColumnIndex(attendanceConstants.SITE_ID)).equals(siteID)
                && cursor.getString(cursor.getColumnIndex(attendanceConstants.WORKER_EMAIL)).equals(workerEmail)) {
                populateAttendance(attendance, cursor);
                allAttendanceList.add(attendance);
            }
            return attendance;

        }
        else
            return null;
    }
    public void populateAttendance(Attendance attendance, Cursor cursor){
        attendance.setUID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(attendanceConstants.UID))));
        attendance.setSiteID(cursor.getString(cursor.getColumnIndex(attendanceConstants.SITE_ID)));
        attendance.setWorkerEmail(cursor.getString(cursor.getColumnIndex(attendanceConstants.WORKER_EMAIL)));
    }

    public int deleteRow(String email){
        // delete all row that has username like this
        db = helper.getWritableDatabase();
        String[] whereArgs = new String[]{String.valueOf(email)};
        return db.delete(attendanceConstants.TABLE_NAME, attendanceConstants.WORKER_EMAIL + "=?", whereArgs);
    }
}

