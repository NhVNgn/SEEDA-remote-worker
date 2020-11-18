package com.sereem.remoteworker.model.workSite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbSiteHelper extends SQLiteOpenHelper {
    private Context context;

    private static final String CREATE_TABLE =
            "CREATE TABLE "+
                    SiteConstants.TABLE_NAME + " (" +
                    SiteConstants.UID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SiteConstants.NAME + " TEXT, " +
                    SiteConstants.SITE_ID + " TEXT, " +
                    SiteConstants.LOCATION + " TEXT, " +
                    SiteConstants.HOURS + " TEXT, " +
                    SiteConstants.MASTER_POINT + " TEXT);" ;

    private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + SiteConstants.TABLE_NAME;

    public dbSiteHelper(Context context){
        super (context, SiteConstants.DATABASE_NAME, null, SiteConstants.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase dbSite) {
        try {
            dbSite.execSQL(CREATE_TABLE);
        } catch (SQLException e) {
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase dbSite, int oldVersion, int newVersion) {
        try {
            dbSite.execSQL(DROP_TABLE);
            onCreate(dbSite);
        } catch (SQLException e) {
        }
    }
}
