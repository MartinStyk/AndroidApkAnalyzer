package sk.styk.martin.apkanalyzer.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

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
        return isConnectionAllowedByUser(context) && hasInternetAccess(context);
    }

    public static boolean isConnectionAllowedByUser(Context context) {
        return new SharedPreferencesHelper(context).readBoolean(USER_CONNECT_ALLOWED);
    }

    public static void setConnectionAllowedByUser(Context context, boolean value) {
        new SharedPreferencesHelper(context).putBoolean(USER_CONNECT_ALLOWED, value);
    }


    public static boolean hasInternetAccess(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection)
                        (new URL("http://clients3.google.com/generate_204")
                                .openConnection());
                urlc.setRequestProperty("User-Agent", "Android");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 204 &&
                        urlc.getContentLength() == 0);
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
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
}
