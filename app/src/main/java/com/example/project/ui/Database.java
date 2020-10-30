package com.example.project.ui;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
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
}
