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

        if (appDetailData == null || appDetailData.getGeneralData() == null || appDetailData.getGeneralData().getPackageName() == null)
            return false;

        ContentValues contentValues = new ContentValues();
        contentValues.put(SendDataEntry.COLUMN_PACKAGE_NAME, appDetailData.getGeneralData().getPackageName());
        contentValues.put(SendDataEntry.COLUMN_VERSION, appDetailData.getGeneralData().getVersionCode());

        return context.getContentResolver().insert(ApkAnalyzerContract.SendDataEntry.CONTENT_URI, contentValues) != null;
    }

    public static boolean isAlreadyUploaded(AppDetailData data, Context context) {
        if (data == null || data.getGeneralData() == null || data.getGeneralData().getPackageName() == null) {
            return true;
        }
        return isAlreadyUploaded(data.getGeneralData().getPackageName(), data.getGeneralData().getVersionCode(), context);
    }

    public static boolean isAlreadyUploaded(String packageName, int packageVersion, Context context) {
        if (packageName == null)
            return true;

        String[] selectionArgs = {packageName, String.valueOf(packageVersion)};

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(ApkAnalyzerContract.SendDataEntry.CONTENT_URI,
                    new String[]{SendDataEntry.COLUMN_PACKAGE_NAME},
                    SendDataEntry.COLUMN_PACKAGE_NAME + "=? AND " + SendDataEntry.COLUMN_VERSION + "=?",
                    selectionArgs, null);

            return cursor != null && cursor.getCount() > 0;
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

}
