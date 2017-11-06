package sk.styk.martin.apkanalyzer.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Martin Styk on 06.11.2017.
 */

public class SharedPreferencesHelper {

    SharedPreferences sharedPreferences;

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String readString(String key) {
        return sharedPreferences.getString(key, null);
    }

}
