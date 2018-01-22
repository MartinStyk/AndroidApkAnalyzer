package sk.styk.martin.apkanalyzer.business.task.upload

import android.content.Context
import android.os.AsyncTask
import android.util.Log
import sk.styk.martin.apkanalyzer.database.service.SendDataService
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.model.server.ServerSideAppData
import sk.styk.martin.apkanalyzer.util.AndroidIdHelper
import sk.styk.martin.apkanalyzer.util.JsonSerializationUtils
import sk.styk.martin.apkanalyzer.util.networking.ConnectivityHelper
import sk.styk.martin.apkanalyzer.util.networking.UploadAppDataRestHelper
import java.lang.ref.WeakReference

/**
 * Encapsulates workflow logic for uploading single app data to server.
 *
 * @author Martin Styk
 * @version 6.11.2017.
 */
class AppDataUploadTask(context: Context) : AsyncTask<AppDetailData?, Void, Void>() {

    private val TAG = AppDataUploadTask::class.java.simpleName

    private val contextWeakReference = WeakReference(context)
    private val jsonSerializationUtils = JsonSerializationUtils()

    public override fun doInBackground(vararg inputData: AppDetailData?): Void? {
        val data = inputData[0] ?: return null

        val packageName = data.generalData.packageName

        Log.i(TAG, "Starting save task for " + packageName)

        val context = contextWeakReference.get() ?: return null

        if (SendDataService.isAlreadyUploaded(data, context)) {
            Log.i(TAG, String.format("Package %s already uploaded", packageName))
            return null
        }

        if (!ConnectivityHelper.isUploadPossible(context)) {
            Log.i(TAG, String.format("Upload not possible, aborting upload of %s", packageName))
            return null
        }

        val uploadData = ServerSideAppData(data, AndroidIdHelper.getAndroidId(context))

        try {
            val responseCode = UploadAppDataRestHelper().postData(jsonSerializationUtils.serialize(uploadData), packageName)
            when (responseCode) {
                201 -> {
                    SendDataService.insert(data, context)
                    Log.i(TAG, String.format("Upload of package %s successful", packageName))
                }
                409 -> {
                    SendDataService.insert(data, context)
                    Log.i(TAG, String.format("Package %s was already uplaoded, however client is not aware of it", packageName))
                }
            }

            Log.i(TAG, String.format("Finished uploading package %s with response + %03d", packageName, responseCode))
        } catch (e: Exception) {
            Log.w(TAG, String.format("Upload of package %s failed with exception %s", packageName, e.toString()))
        }

        Log.i(TAG, "Finishing save task for " + packageName)

        return null
    }
}