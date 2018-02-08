package sk.styk.martin.apkanalyzer.util.networking

import android.support.annotation.WorkerThread
import android.util.Log
import okhttp3.Request
import java.io.IOException

/**
 * @author Martin Styk
 * @version 11/8/17.
 */
@WorkerThread
object RepackagedDetectionServerHelper : ServerHttpAccessHelper() {
    private val TAG = RepackagedDetectionServerHelper::class.java.name

    @Throws(IOException::class)
    fun get(appHash: Int, packageName: String): Pair<Int, String?> {
        val request = Request.Builder()
                .url(ServerUrls.URL_BASE + ServerUrls.REPACKAGED_DETECTION_PATH + "?app_hash=" + appHash)
                .build()

        client.newCall(request).execute().use { response ->
            Log.i(TAG, String.format("Response on getting %s data is %s", packageName, response.toString()))
            return Pair(response.code(), response.body()?.string())
        }
    }
}