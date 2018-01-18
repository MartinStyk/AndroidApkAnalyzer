package sk.styk.martin.apkanalyzer.database.service

import android.content.ContentValues
import android.content.Context
import sk.styk.martin.apkanalyzer.database.ApkAnalyzerContract
import sk.styk.martin.apkanalyzer.database.ApkAnalyzerContract.SendDataEntry
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData

/**
 * @author Martin Styk
 * @version 07.11.2017.
 */
object SendDataService {

    fun insert(appDetailData: AppDetailData?, context: Context): Boolean {

        if (appDetailData?.generalData?.packageName == null)
            return false

        val contentValues = ContentValues()
        contentValues.put(SendDataEntry.COLUMN_PACKAGE_NAME, appDetailData.generalData.packageName)
        contentValues.put(SendDataEntry.COLUMN_VERSION, appDetailData.generalData.versionCode)

        return context.contentResolver.insert(ApkAnalyzerContract.SendDataEntry.CONTENT_URI, contentValues) != null
    }

    fun isAlreadyUploaded(data: AppDetailData?, context: Context): Boolean {
        return if (data?.generalData?.packageName == null) {
            true
        } else isAlreadyUploaded(data.generalData.packageName, data.generalData.versionCode, context)
    }

    fun isAlreadyUploaded(packageName: String?, packageVersion: Int, context: Context): Boolean {
        packageName ?: return true

        val selectionArgs = arrayOf(packageName, packageVersion.toString())

        return try {
            context.contentResolver.query(ApkAnalyzerContract.SendDataEntry.CONTENT_URI,
                    arrayOf(SendDataEntry.COLUMN_PACKAGE_NAME),
                    SendDataEntry.COLUMN_PACKAGE_NAME + "=? AND " + SendDataEntry.COLUMN_VERSION + "=?",
                    selectionArgs, null)
                    .use {
                        it != null && it.count > 0
                    }
        } catch (e: Exception) {
            false
        }
    }

}
