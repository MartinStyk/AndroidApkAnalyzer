package sk.styk.martin.apkanalyzer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import sk.styk.martin.apkanalyzer.database.ApkAnalyzerContract.SendDataEntry;

/**
 * Created by Martin Styk on 06.11.2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "apk.analyzer.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String SQL_CREATE_VEHICLE_TYPES_TABLE = "CREATE TABLE " + SendDataEntry.TABLE_NAME + " ("
                + SendDataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SendDataEntry.COLUMN_PACKAGE_NAME + " TEXT NOT NULL, "
                + SendDataEntry.COLUMN_VERSION + " INTEGER NOT NULL, "
                + SendDataEntry.COLUMN_TIMESTAMP + "TIMESTAMP DEFAULT CURRENT_TIMESTAMP );";

        db.execSQL(SQL_CREATE_VEHICLE_TYPES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
