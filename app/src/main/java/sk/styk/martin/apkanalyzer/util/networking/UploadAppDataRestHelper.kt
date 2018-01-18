package sk.styk.martin.apkanalyzer.util.networking

import android.util.Log

import java.io.IOException

import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response

import sk.styk.martin.apkanalyzer.util.networking.ServerUrls.UPLOAD_RECORD_PATH
import sk.styk.martin.apkanalyzer.util.networking.ServerUrls.URL_BASE

/**
 * @author Martin Styk
 * @version 10.12.2017.
 */

class UploadAppDataRestHelper : ServerHttpAccessHelper() {

    private val TAG = ServerHttpAccessHelper::class.java.name

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
