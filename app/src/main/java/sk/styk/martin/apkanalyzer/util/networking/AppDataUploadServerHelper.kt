package sk.styk.martin.apkanalyzer.util.networking

import android.support.annotation.WorkerThread
import android.util.Log
import okhttp3.Request
import okhttp3.RequestBody
import java.io.IOException

/**
 * @author Martin Styk
 * @version 11/8/17.
 */
@WorkerThread
object AppDataUploadServerHelper : ServerHttpAccessHelper() {
    private val TAG = AppDataUploadServerHelper::class.java.name

    @Throws(IOException::class)
    fun postData(json: String, packageName: String): Int {
        val body = RequestBody.create(ServerHttpAccessHelper.Companion.JSON, zipContent(json).toByteArray())

        val request = Request.Builder()
                .url(ServerUrls.URL_BASE + ServerUrls.UPLOAD_RECORD_PATH)
                .post(body)
                .header("Content-Encoding", "gzip")
                .build()

        client.newCall(request).execute().use { response ->
            Log.i(TAG, String.format("Response on posting %s data is %s", packageName, response.toString()))
            return response.code()
        }
    }
}