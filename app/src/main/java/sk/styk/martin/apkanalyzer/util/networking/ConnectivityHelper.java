package sk.styk.martin.apkanalyzer.util.networking;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import sk.styk.martin.apkanalyzer.util.SharedPreferencesHelper;

import static sk.styk.martin.apkanalyzer.util.networking.ServerUrls.GENERATE_200;
import static sk.styk.martin.apkanalyzer.util.networking.ServerUrls.URL_BASE;

/**
 * Created by mstyk on 11/8/17.
 */

public class ConnectivityHelper {


    public static final String USER_CONNECT_ALLOWED = "user_connect_allowed";

    private static final String TAG = ConnectivityHelper.class.getCanonicalName();

    /**
     * Checks user preferences for upload allowance and internet connectivity.
     *
     * @return true in case everything permits upload
     */
    public static boolean isUploadPossible(Context context) {
        return isConnectionAllowedByUser(context) && hasAccessToServer(context) && isWifiOnAndConnected(context);
    }

    public static boolean isConnectionAllowedByUser(Context context) {
        return new SharedPreferencesHelper(context).readBoolean(USER_CONNECT_ALLOWED);
    }

    public static void setConnectionAllowedByUser(Context context, boolean value) {
        new SharedPreferencesHelper(context).putBoolean(USER_CONNECT_ALLOWED, value);
    }


    public static boolean hasAccessToServer(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL(URL_BASE + GENERATE_200)
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(10000);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e(TAG, "Error checking internet connection", e);
            }
        } else {
            Log.d(TAG, "No network available!");
        }
        return false;
    }

    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null)
            return false;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private static boolean isWifiOnAndConnected(Context context) {
        WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiManager != null && wifiManager.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifiManager.getConnectionInfo();

            return wifiInfo.getNetworkId() != -1;
        }

        return false; // Wi-Fi adapter is OFF
    }


}
