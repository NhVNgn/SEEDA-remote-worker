package com.example.project.model;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public class Database {
    private SQLiteDatabase db;
    private Context context;
    private final dbHelper helper;
    Constants constants = new Constants();
    public Database (Context c){
        context = c;
        helper = new dbHelper(context);
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
    }
}