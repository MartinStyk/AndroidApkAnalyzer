package sk.styk.martin.apkanalyzer.database.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import sk.styk.martin.apkanalyzer.database.ApkAnalyzerContract;
import sk.styk.martin.apkanalyzer.database.ApkAnalyzerContract.SendDataEntry;
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData;

/**
 * Created by Martin Styk on 07.11.2017.
 */
public class SendDataService {

    public static boolean insert(AppDetailData appDetailData, Context context) {

        if (appDetailData == null)
            return false;

        ContentValues contentValues = new ContentValues();
        contentValues.put(SendDataEntry.COLUMN_HASH, appDetailData.hashCode());
        contentValues.put(SendDataEntry.COLUMN_PACKAGE_NAME, appDetailData.getGeneralData().getPackageName());
        contentValues.put(SendDataEntry.COLUMN_VERSION, appDetailData.getGeneralData().getVersionCode());

        return context.getContentResolver().insert(ApkAnalyzerContract.SendDataEntry.CONTENT_URI, contentValues) != null;
    }

    public static boolean isAlreadyUploaded(AppDetailData appDetailData, Context context) {
        String[] selectionArgs = {String.valueOf(appDetailData.hashCode())};

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(ApkAnalyzerContract.SendDataEntry.CONTENT_URI,
                    new String[]{ApkAnalyzerContract.SendDataEntry.COLUMN_HASH}, ApkAnalyzerContract.SendDataEntry.COLUMN_HASH + "=?",
                    selectionArgs, null);

            return cursor != null && cursor.getCount() > 0;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }
}
