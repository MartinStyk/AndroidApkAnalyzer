package sk.styk.martin.apkanalyzer.util.networking

import android.content.Context
import android.net.ConnectivityManager
import android.net.wifi.WifiManager
import android.support.annotation.AnyThread
import android.support.annotation.WorkerThread
import android.util.Log
import sk.styk.martin.apkanalyzer.util.SharedPreferencesHelper
import sk.styk.martin.apkanalyzer.util.networking.ServerUrls.GENERATE_200
import sk.styk.martin.apkanalyzer.util.networking.ServerUrls.URL_BASE
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

/**
 * @author Martin Styk
 * @version 11/8/17.
 */
object ConnectivityHelper {

    private const val USER_CONNECT_ALLOWED = "user_connect_allowed"
    private val TAG = ConnectivityHelper::class.java.canonicalName

    /**
     * Checks user preferences for upload allowance and internet connectivity.
     *
     * @return true in case everything permits upload
     */
    @WorkerThread
    fun isUploadPossible(context: Context): Boolean {
        return isConnectionAllowedByUser(context) && hasAccessToServer(context) && isWifiOnAndConnected(context)
    }

    @AnyThread
    fun isConnectionAllowedByUser(context: Context): Boolean {
        return SharedPreferencesHelper(context).readBoolean(USER_CONNECT_ALLOWED)
    }

    @AnyThread
    fun setConnectionAllowedByUser(context: Context, value: Boolean) {
        SharedPreferencesHelper(context).putBoolean(USER_CONNECT_ALLOWED, value)
    }

    @WorkerThread
    fun hasAccessToServer(context: Context): Boolean {
        if (isNetworkAvailable(context)) {
            var connection: HttpURLConnection? = null
            try {
                connection = URL(URL_BASE + GENERATE_200)
                        .openConnection() as HttpURLConnection
                connection.setRequestProperty("User-Agent", "Android")
                connection.setRequestProperty("Connection", "close")
                connection.connectTimeout = 3000
                connection.connect()
                return connection.responseCode == 200
            } catch (e: IOException) {
                Log.e(TAG, "Error checking internet connection", e)
            } finally {
                if (connection != null) {
                    connection.disconnect()
                }
            }
        } else {
            Log.d(TAG, "No network available!")
        }
        return false
    }

    @AnyThread
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo

        return activeNetworkInfo != null
    }

    @AnyThread
    private fun isWifiOnAndConnected(context: Context): Boolean {
        val wifiManager = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        if (wifiManager.isWifiEnabled) { // Wi-Fi adapter is ON

            val networkId = wifiManager.connectionInfo?.networkId

            return networkId != null && networkId != -1
        }

        return false // Wi-Fi adapter is OFF
    }


}
