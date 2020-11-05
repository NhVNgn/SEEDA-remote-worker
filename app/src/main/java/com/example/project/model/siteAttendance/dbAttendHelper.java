package com.example.project.model.siteAttendance;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.project.model.siteAttendance.attendanceConstants;

public class dbAttendHelper extends SQLiteOpenHelper {
    private Context context;


    private static final String CREATE_TABLE =
            "CREATE TABLE "+
                    attendanceConstants.TABLE_NAME + " (" +
                    attendanceConstants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    attendanceConstants.WORKER_EMAIL + " TEXT, " +
                    attendanceConstants.SITE_ID + " TEXT);" ;

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + attendanceConstants.TABLE_NAME;

    public dbAttendHelper(Context context){
        super (context, attendanceConstants.DATABASE_NAME, null, attendanceConstants.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase dbAttendance) {
        try {
            dbAttendance.execSQL(CREATE_TABLE);
            Toast.makeText(context, "onCreate() called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "exception onCreate() db", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase dbAttendance, int oldVersion, int newVersion) {
        try {
            dbAttendance.execSQL(DROP_TABLE);
            onCreate(dbAttendance);
            Toast.makeText(context, "onUpgrade called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "exception onUpgrade() db", Toast.LENGTH_LONG).show();
        }
    }
}
