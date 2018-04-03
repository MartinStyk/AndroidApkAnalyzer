package sk.styk.martin.apkanalyzer.business.upload.task

import android.content.Context
import android.util.Log
import sk.styk.martin.apkanalyzer.business.base.task.ApkAnalyzerAbstractAsyncLoader
import sk.styk.martin.apkanalyzer.business.database.service.SendDataService
import sk.styk.martin.apkanalyzer.business.upload.logic.AppDataUploadService
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.model.server.RepackagedDetectionResult
import sk.styk.martin.apkanalyzer.model.server.ServerSideAppData
import sk.styk.martin.apkanalyzer.util.AndroidIdHelper
import sk.styk.martin.apkanalyzer.util.JsonSerializationUtils
import sk.styk.martin.apkanalyzer.util.networking.ApkAnalyzerApi
import sk.styk.martin.apkanalyzer.util.networking.ConnectivityHelper
import java.lang.ref.WeakReference
import java.net.SocketTimeoutException

/**
 * @author Martin Styk
 * @version 15.01.2018
 */
class RepackagedDetectionLoader(val data: AppDetailData, context: Context) : ApkAnalyzerAbstractAsyncLoader<RepackagedDetectionLoader.LoaderResult>(context) {
    private val TAG = RepackagedDetectionLoader::class.java.simpleName

    private var callbackReference: WeakReference<RepackagedDetectionLoader.ProgressCallback>? = null

    interface ProgressCallback {
        fun onProgressChanged(status: LoaderStatus = LoaderStatus.UPLOADING)
    }

    fun setCallback(progressCallback: RepackagedDetectionLoader.ProgressCallback) {
        callbackReference = WeakReference(progressCallback)
    }

    override fun loadInBackground(): LoaderResult {
        val packageName = data.generalData.packageName

        if (!ConnectivityHelper.isNetworkAvailable(context)) {
            Log.i(TAG, String.format("Not connected to internet"))
            return LoaderResult.NotConnectedToInternet
        }

        if (!ConnectivityHelper.isConnectionAllowedByUser(context)) {
            Log.i(TAG, String.format("Connection to server not allowed by user"))
            return LoaderResult.UserNotAllowedUpload
        }

        if (!ConnectivityHelper.hasAccessToServer(context)) {
            Log.i(TAG, String.format("Apk Analyzer server is not available"))
            return LoaderResult.ServiceNotAvailable
        }

        val uploadData = ServerSideAppData(data, AndroidIdHelper.getAndroidId(context))

        // in case of uploaded data, run detection. If it fails, try to re-upload data and rerun detection
        if (SendDataService.isAlreadyUploaded(data, context)) {
            val result = runDetection(uploadData, packageName)
            if (result != null)
                return result
        }

        Log.i(TAG, String.format("Trying to upload package %s", packageName))
        callbackReference?.get()?.onProgressChanged(LoaderStatus.UPLOADING)

        val isDataOnServer = AppDataUploadService().uploadServerSideDataWithoutValidations(uploadData)

        if (!isDataOnServer) {
            Log.w(TAG, String.format("Could not getRepackagedDetectionResult the data to server"))
            return LoaderResult.CommunicationError
        }

        return runDetection(uploadData, packageName) ?: LoaderResult.CommunicationError
    }

    private fun runDetection(uploadData: ServerSideAppData, packageName: String): LoaderResult? {
        Log.i(TAG, String.format("Running detection of %s", packageName))
        callbackReference?.get()?.onProgressChanged(LoaderStatus.DETECTING)
        try {
            val (responseCode, responseBody) = ApkAnalyzerApi.instance.getRepackagedDetectionResult(uploadData.appHash, packageName)
            if (responseCode == 200) {
                val result = JsonSerializationUtils().deserialize<RepackagedDetectionResult>(responseBody.toString())
                return LoaderResult.Success(result)
            }
        } catch (e: SocketTimeoutException) {
            return LoaderResult.LongOperationError
        } catch (e: Throwable) {
            Log.w(TAG, String.format("Checking of package %s failed with exception %s", uploadData.packageName, e.toString()))
        }
    return null
}

companion object {
    const val ID = 7
}

sealed class LoaderResult {
    object NotConnectedToInternet : LoaderResult()
    object UserNotAllowedUpload : LoaderResult()
    object ServiceNotAvailable : LoaderResult()
    object CommunicationError : LoaderResult()
    object LongOperationError : LoaderResult()
    class Success(val result: RepackagedDetectionResult) : LoaderResult()
}

enum class LoaderStatus {
    UPLOADING, DETECTING
}

}

