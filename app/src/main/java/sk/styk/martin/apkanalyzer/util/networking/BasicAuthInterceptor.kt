package sk.styk.martin.apkanalyzer.util.networking

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Interceptor for authentication to server
 * This interceptor adds auth header to every request
 *
 * @author Martin Styk
 * @version 10.12.2017.
 */
class BasicAuthInterceptor : Interceptor {

    private val credentials: String = Credentials.basic(USER, PASSWORD)

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val authenticatedRequest = request.newBuilder()
                .header("Authorization", credentials).build()
        return chain.proceed(authenticatedRequest)
    }

    companion object {
        private const val USER = "device"
        private const val PASSWORD = "******"
    }

}