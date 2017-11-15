package sk.styk.martin.apkanalyzer.util;

import android.content.Context;

/**
 * Created by mstyk on 11/15/17.
 */

public class FirstStartHelper {

    public static final String FIRST_APP_START = "first_app_start";


    public static boolean check(Context context) {

        SharedPreferencesHelper preferencesHelper = new SharedPreferencesHelper(context);
        boolean isFirstStart = preferencesHelper.readBoolean(FIRST_APP_START, true);

        if (isFirstStart) {
            preferencesHelper.putBoolean(FIRST_APP_START, false);
        }

        return isFirstStart;
    }
}
