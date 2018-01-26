package sk.styk.martin.apkanalyzer.business.upload.task

import android.content.Context
import android.util.Log
import sk.styk.martin.apkanalyzer.business.base.task.ApkAnalyzerAbstractAsyncLoader
import sk.styk.martin.apkanalyzer.business.upload.logic.AppDataUploadService
import sk.styk.martin.apkanalyzer.database.service.SendDataService
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.model.server.ServerSideAppData
import sk.styk.martin.apkanalyzer.util.AndroidIdHelper
import sk.styk.martin.apkanalyzer.util.networking.ConnectivityHelper
import sk.styk.martin.apkanalyzer.util.networking.RepackagedDetectionServerHelper

/**
 * @author Martin Styk
 * @version 15.01.2018
 */
class RepackagedDetectionLoader(val data: AppDetailData, context: Context) : ApkAnalyzerAbstractAsyncLoader<String>(context) {
    private val TAG = RepackagedDetectionLoader::class.java.simpleName

    override fun loadInBackground(): String {
        val packageName = data.generalData.packageName

        if (!ConnectivityHelper.isConnectionAllowedByUser(context)) {
            Log.i(TAG, String.format("Connection to server not allowed by user"))
            return "Connection to server not allowed by user";
        }

        if (!ConnectivityHelper.hasAccessToServer(context)) {
            Log.i(TAG, String.format("Apk Analyzer server is not available"))
            return "Apk Analyzer server is not available";
        }

        val uploadData = ServerSideAppData(data, AndroidIdHelper.getAndroidId(context))

        if (!SendDataService.isAlreadyUploaded(data, context)) {
            Log.i(TAG, String.format("Package %s not already uploaded", packageName))
            val isDataOnServer = AppDataUploadService().uploadServerSideDataWithoutValidations(uploadData)

            if (!isDataOnServer) {
                Log.w(TAG, String.format("Could not get the data to server"))
                return "Could not get the data to server";
            }
        }

        try {
            val (responseCode, responseBody) = RepackagedDetectionServerHelper.get(uploadData.appHash, packageName)
//            when (responseCode) {
//                200 -> {
//                    // TODO gson unzip body
//                }
//                else -> return "error"
//            }
            return responseBody.toString()
        } catch (e: Exception) {
            Log.w(TAG, String.format("Checking of package %s failed with exception %s", uploadData.packageName, e.toString()))
        }

        return "This is baaad"
    }

    companion object {
        const val ID = 7
    }

}

