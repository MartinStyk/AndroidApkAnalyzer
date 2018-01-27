package sk.styk.martin.apkanalyzer.util.networking

import okhttp3.MediaType
import okhttp3.OkHttpClient
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.util.zip.GZIPOutputStream

/**
 * @author Martin Styk
 * @version 12.11.2017.
 */
open class ServerHttpAccessHelper {

    internal var client = initClient()

    @Throws(IOException::class)
    protected fun zipContent(json: String): ByteArrayOutputStream {
        val data = json.toByteArray(charset("UTF-8"))

        val outputStream = ByteArrayOutputStream()

        GZIPOutputStream(outputStream).use { zipper ->
            zipper.write(data)
        }

        return outputStream
    }

    private fun initClient(): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(BasicAuthInterceptor())
                .build()
    }

    companion object {
        val JSON = MediaType.parse("application/json; charset=utf-8")
    }
}
