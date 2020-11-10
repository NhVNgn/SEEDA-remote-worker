package com.example.project.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Database {
    private SQLiteDatabase db;
    private Context context;
    private final dbHelper helper;
    private List<User> defaultUserList = new ArrayList<>();
    Constants constants = new Constants();
    public Database (Context c){
        context = c;
        helper = new dbHelper(context);

        addSample("Denys", "", "dovsiien@sfu.ca", "123", "111", "", "",
                    "", "", "", "", "", "");
        addSample("Vy", "", "nhanvyn@sfu.ca", "123", "111", "", "",
                "", "", "", "", "", "");

        addSample("Vy2", "", "nhanvyhl1234@gmail.com", "123", "111", "", "",
                "", "", "", "", "", "");

        addSample("Vy3", "", "nhanvyhl1234@gmail.com", "123", "111", "", "",
                "", "", "", "", "", "");

    }

    public long insertData (List<String> args)
    {
        db = helper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        for (int i = 0; i < args.size(); i++){
            contentValues.put(constants.arrayOfConst.get(i), args.get(i));
        }
        long id = db.insert(Constants.TABLE_NAME, null, contentValues);
        return id;
    }

    @SuppressLint("Recycle")
    public User getUser(String email, String password)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM " + Constants.TABLE_NAME +
                " WHERE " + Constants.EMAIL + " =?";
        Cursor cursor = db.rawQuery(sql, new String[] {email});
        User user = new User();
        if(cursor.moveToFirst()) {
            if(cursor.getString(cursor.getColumnIndex(Constants.PASSWORD)).equals(password)) {
                fillUser(user, cursor);
            } else {
                user.setFirstName("INCORRECT_PASSWORD");
            }
        } else {
            user.setFirstName("NOT_FOUND");
        }
        return user;
    }

    public User getUser(Integer id)
    {
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM " + Constants.TABLE_NAME +
                " WHERE " + Constants.UID + " =?";
        Cursor cursor = db.rawQuery(sql, new String[] {id.toString()});
        User user = new User();
        if(cursor.moveToFirst()) {
            fillUser(user, cursor);
        } else {
            user.setFirstName("NOT_FOUND");
        }
        return user;
    }

    public User getUserByEmail(String email){
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM " + Constants.TABLE_NAME +
                " WHERE " + Constants.EMAIL + " =?";
        Cursor cursor = db.rawQuery(sql, new String[] {email});
        User user = new User();
        if(cursor.moveToFirst()) {
            fillUser(user, cursor);
        } else {
            user.setFirstName("NOT_FOUND");
        }
        return user;
    }

    public int update(int id, ContentValues cv) {
        SQLiteDatabase db = helper.getReadableDatabase();
        return db.update(Constants.TABLE_NAME, cv, "_id=" + id, null);
    }

    private User getDefaultUser(String email, String password){
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT * FROM " + Constants.TABLE_NAME +
                " WHERE " + Constants.EMAIL + " =?";
        Cursor cursor = db.rawQuery(sql, new String[] {email});
        User user = new User();
        if (cursor.moveToFirst()){
            if (cursor.getString(cursor.getColumnIndex(Constants.PASSWORD)).equals(password)){
                fillUser(user, cursor);
                defaultUserList.add(user);
            }
            return user;
        }
        else
            return null;
    }
    private void addSample(String firstName, String lastName, String email, String password, String phone,
                           String birthday, String companyID, String emFirstName, String emLastName, String emPhone,
                           String emRelation, String MedConsider, String IconRes){
        if (getDefaultUser(email, password) != null)
            return;

        List<String> argsArray = new ArrayList<>();
        argsArray.add(firstName);
        argsArray.add(lastName);
        argsArray.add(email);
        argsArray.add(password);
        argsArray.add(phone);
        argsArray.add(birthday);
        argsArray.add(companyID);
        argsArray.add(emFirstName);
        argsArray.add(emLastName);
        argsArray.add(emPhone);
        argsArray.add(emRelation);
        argsArray.add(MedConsider);
        argsArray.add(IconRes);

        long id = insertData(argsArray);
        if (id < 0)
        {
            System.out.println("fail to add user");
        }
        else {
            System.out.println("user added successfully");
            User newUser = new User(firstName, lastName, email, password, phone,
                    birthday, companyID,  emFirstName,  emLastName,  emPhone,
                     emRelation,  MedConsider,  IconRes.getBytes());
            defaultUserList.add(newUser);
        }
    }
    private void fillUser(User user, Cursor cursor) {
        user.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Constants.UID))));
        user.setCompanyID((cursor.getString(cursor.getColumnIndex(Constants.COMPANY_ID))));
        user.setFirstName(cursor.getString(cursor.getColumnIndex(Constants.FIRST_NAME)));
        user.setLastName(cursor.getString(cursor.getColumnIndex(Constants.LAST_NAME)));
        user.setEmail(cursor.getString(cursor.getColumnIndex(Constants.EMAIL)));
        user.setPassword(cursor.getString(cursor.getColumnIndex(Constants.PASSWORD)));
        user.setPhone(cursor.getString(cursor.getColumnIndex(Constants.PHONE)));
        user.setBirthday(cursor.getString(cursor.getColumnIndex(Constants.BIRTHDAY)));
        user.setEmFirstName(cursor.getString(cursor.getColumnIndex(Constants.EM_FIRST_NAME)));
        user.setEmLastName(cursor.getString(cursor.getColumnIndex(Constants.EM_LAST_NAME)));
        user.setEmPhone(cursor.getString(cursor.getColumnIndex(Constants.EM_PHONE)));
        user.setEmRelation(cursor.getString(cursor.getColumnIndex(Constants.EM_RELATION)));
        user.setMedicalConsiderations(cursor.getString(cursor.getColumnIndex(Constants.MED_CONSIDERATIONS)));
        user.setIconRes(cursor.getBlob(cursor.getColumnIndex(Constants.ICON_RES)));
    }
}
