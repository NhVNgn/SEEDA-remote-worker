package com.sereem.remoteworker.model.siteAttendance;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * attendanceConstants class, not used by the application, used in iteration 2 to support
 *  * attendance database. Used for creating tables in attendance database.
 */
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
        } catch (SQLException e) {
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase dbAttendance, int oldVersion, int newVersion) {
        try {
            dbAttendance.execSQL(DROP_TABLE);
            onCreate(dbAttendance);
        } catch (SQLException e) {
        }
    }
}
