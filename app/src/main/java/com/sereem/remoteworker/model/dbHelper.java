package com.sereem.remoteworker.model;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * dbHelper class, not used by the application, used in iteration 2 to support
 *  * user database. Used for creating tables in user database.
 */
public class dbHelper extends SQLiteOpenHelper {


    private Context context;

    private static final String CREATE_TABLE =
            "CREATE TABLE "+
                    Constants.TABLE_NAME + " (" +
                    Constants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.FIRST_NAME + " TEXT, " +
                    Constants.LAST_NAME + " TEXT, " +
                    Constants.EMAIL + " TEXT, " +
                    Constants.PASSWORD + " TEXT, " +
                    Constants.PHONE + " TEXT, " +
                    Constants.BIRTHDAY + " TEXT, " +
                    Constants.COMPANY_ID + " TEXT, " +
                    Constants.EM_FIRST_NAME + " TEXT, " +
                    Constants.EM_LAST_NAME + " TEXT, " +
                    Constants.EM_PHONE + " TEXT, " +
                    Constants.EM_RELATION + " TEXT, " +
                    Constants.MED_CONSIDERATIONS + " TEXT, " +
                    Constants.ICON_URI + " TEXT);" ;

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + Constants.TABLE_NAME;

    public dbHelper(Context context){
        super (context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_TABLE);
        } catch (SQLException e) {
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        } catch (SQLException e) {
        }
    }
}
