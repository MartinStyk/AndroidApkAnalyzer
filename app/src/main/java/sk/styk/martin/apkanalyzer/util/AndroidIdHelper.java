package sk.styk.martin.apkanalyzer.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;

import java.util.UUID;

/**
 * Unique Android device identification helper.
 *
 * @author Martin Styk
 * @version 06.11.2017.
 */
public class AndroidIdHelper {

    public static final String ANDROID_ID = "android_id";

    public static String getAndroidId(Context context) {
        @SuppressLint("HardwareIds")
        String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);

        if (androidId != null)
            return androidId;

        // if android id is not found, look for generated value in shared preferences
        SharedPreferencesHelper sharedPreferencesHelper = new SharedPreferencesHelper(context);
        String fromPreferences = sharedPreferencesHelper.readString(ANDROID_ID);

        if (fromPreferences != null)
            return fromPreferences;

        // if no value is found in preferences, generate and save it
        String generatedId = UUID.randomUUID().toString();
        sharedPreferencesHelper.putString(ANDROID_ID, generatedId);

        return generatedId;
    }
}
