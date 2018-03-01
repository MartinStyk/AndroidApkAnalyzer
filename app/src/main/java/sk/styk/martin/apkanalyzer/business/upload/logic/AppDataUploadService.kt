package sk.styk.martin.apkanalyzer.business.upload.logic

import android.content.Context
import android.support.annotation.WorkerThread
import android.util.Log
import sk.styk.martin.apkanalyzer.ApkAnalyzer.Companion.context
import sk.styk.martin.apkanalyzer.business.database.service.SendDataService
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.model.server.ServerSideAppData
import sk.styk.martin.apkanalyzer.util.AndroidIdHelper
import sk.styk.martin.apkanalyzer.util.JsonSerializationUtils
import sk.styk.martin.apkanalyzer.util.networking.ApkAnalyzerApi
import sk.styk.martin.apkanalyzer.util.networking.ConnectivityHelper
import java.lang.ref.WeakReference

/**
 * Encapsulates workflow logic for uploading single app data to server.
 *
 * @author Martin Styk
 * @version 26.01.2018.
 */
@WorkerThread
class AppDataUploadService {

    private val TAG = AppDataUploadService::class.java.simpleName

    private val contextWeakReference = WeakReference<Context>(context)

    /**
     * @return true if data is present on server after this method finishes
     */
    fun upload(data: AppDetailData?): Boolean {
        if (data == null)
            return false

        val packageName = data.generalData.packageName

        Log.i(TAG, "Starting save task for " + packageName)

        val context = contextWeakReference.get() ?: return false

        if (SendDataService.isAlreadyUploaded(data, context)) {
            Log.i(TAG, String.format("Package %s already uploaded", packageName))
            return true
        }

        if (!ConnectivityHelper.isUploadPossible(context)) {
            Log.i(TAG, String.format("Upload not possible, aborting upload of %s", packageName))
            return false
        }

        val uploadData = ServerSideAppData(data, AndroidIdHelper.getAndroidId(context))
        return uploadServerSideDataWithoutValidations(uploadData)
    }

    fun uploadServerSideDataWithoutValidations(serverSideAppData: ServerSideAppData): Boolean {
        var isOnServer = false
        try {
            val responseCode = ApkAnalyzerApi.instance.postAppData(serverSideAppData)
            when (responseCode) {
                201 -> {
                    SendDataService.insert(serverSideAppData, context)
                    Log.i(TAG, String.format("Upload of package %s successful", serverSideAppData.packageName))
                    isOnServer = true
                }
                409 -> {
                    SendDataService.insert(serverSideAppData, context)
                    Log.i(TAG, String.format("Package %s was already uplaoded, however client is not aware of it", serverSideAppData.packageName))
                    isOnServer = true
                }
            }

            Log.i(TAG, String.format("Finished uploading package %s with response + %03d", serverSideAppData.packageName, responseCode))
        } catch (e: Throwable) {
            Log.w(TAG, String.format("Upload of package %s failed with exception %s", serverSideAppData.packageName, e.toString()))
        }

        Log.i(TAG, "Finishing save task for " + serverSideAppData.packageName)

        return isOnServer
    }
}
