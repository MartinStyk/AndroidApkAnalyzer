package sk.styk.martin.apkanalyzer.util;

import android.content.Context;

/**
 * Created by mstyk on 11/15/17.
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
