package com.example.project.model.workSite;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

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
            Toast.makeText(context, "onCreate() called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "exception onCreate() db", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase dbSite, int oldVersion, int newVersion) {
        try {
            dbSite.execSQL(DROP_TABLE);
            onCreate(dbSite);
            Toast.makeText(context, "onUpgrade called", Toast.LENGTH_LONG).show();
        } catch (SQLException e) {
            Toast.makeText(context, "exception onUpgrade() db", Toast.LENGTH_LONG).show();
        }
    }
}
