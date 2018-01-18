package sk.styk.martin.apkanalyzer.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import sk.styk.martin.apkanalyzer.database.ApkAnalyzerContract.SendDataEntry

/**
 * @author Martin Styk
 * @version 06.11.2017.
 */
class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {

        val createDatabase = ("CREATE TABLE " + SendDataEntry.TABLE_NAME + " ("
                + SendDataEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + SendDataEntry.COLUMN_PACKAGE_NAME + " TEXT NOT NULL, "
                + SendDataEntry.COLUMN_VERSION + " INTEGER NOT NULL, "
                + SendDataEntry.COLUMN_TIMESTAMP + "TIMESTAMP DEFAULT CURRENT_TIMESTAMP );")

        db.execSQL(createDatabase)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    companion object {

        private const val DATABASE_NAME = "apk.analyzer.db"
        private const val DATABASE_VERSION = 1
    }
}
