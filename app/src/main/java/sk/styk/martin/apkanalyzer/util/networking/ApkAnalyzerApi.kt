package sk.styk.martin.apkanalyzer.util.networking

import android.support.annotation.WorkerThread
import android.util.Log
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import sk.styk.martin.apkanalyzer.model.server.ServerSideAppData
import sk.styk.martin.apkanalyzer.util.JsonSerializationUtils
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.util.zip.GZIPOutputStream

/**
 * @author Martin Styk
 * @version 12.11.2017.
 */
@WorkerThread
class ApkAnalyzerApi private constructor() {

    private object Holder {
        val INSTANCE = ApkAnalyzerApi()
    }

    companion object {
        val instance: ApkAnalyzerApi by lazy { Holder.INSTANCE }
        val JSON = MediaType.parse("application/json; charset=utf-8")
        val TAG = ApkAnalyzerApi::class.java.simpleName
    }

    private val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(45, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .addInterceptor(BasicAuthInterceptor())
            .build()

    private val jsonSerializationUtils = JsonSerializationUtils()


    @Throws(IOException::class)
    fun getRepackagedDetectionResult(appHash: Int, packageName: String): Pair<Int, String?> {
        val request = Request.Builder()
                .url(ServerUrls.URL_BASE + ServerUrls.REPACKAGED_DETECTION_PATH + "?app_hash=" + appHash)
                .build()

        client.newCall(request).execute().use { response ->
            Log.i(TAG, String.format("Response on getting %s data is %s", packageName, response.toString()))
            return Pair(response.code(), response.body()?.string())
        }
    }

    @Throws(IOException::class)
    fun postAppData(serverSideAppData: ServerSideAppData): Int {
        val serializedData = jsonSerializationUtils.serialize(serverSideAppData)
        val body = RequestBody.create(ApkAnalyzerApi.JSON, zipContent(serializedData).toByteArray())

        val request = Request.Builder()
                .url(ServerUrls.URL_BASE + ServerUrls.UPLOAD_RECORD_PATH)
                .post(body)
                .header("Content-Encoding", "gzip")
                .build()

        client.newCall(request).execute().use { response ->
            Log.i(TAG, String.format("Response on posting %s data is %s", serverSideAppData.packageName, response.toString()))
            return response.code()
        }
    }

    @Throws(IOException::class)
    private fun zipContent(json: String): ByteArrayOutputStream {
        val data = json.toByteArray(charset("UTF-8"))

        val outputStream = ByteArrayOutputStream()

        GZIPOutputStream(outputStream).use { zipper ->
            zipper.write(data)
        }

        return outputStream
    }

}
