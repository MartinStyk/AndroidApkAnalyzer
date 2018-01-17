package sk.styk.martin.apkanalyzer.util;

import android.content.Context;

/**
 * @author Martin Styk
 * @version 15.11.2017.
 */
public class FirstStartHelper {

    public static final String FIRST_APP_START = "first_app_start";


    public static boolean isFirstStart(Context context) {

        SharedPreferencesHelper preferencesHelper = new SharedPreferencesHelper(context);
        return preferencesHelper.readBoolean(FIRST_APP_START, true);
    }

    public static void setFirstStartFinished(Context context) {
        SharedPreferencesHelper preferencesHelper = new SharedPreferencesHelper(context);
        preferencesHelper.putBoolean(FIRST_APP_START, false);
    }
}
