package sk.styk.martin.apkanalyzer.util

import android.content.Context
import android.preference.PreferenceManager

/**
 * @author Martin Styk
 * @version 06.11.2017.
 */

class SharedPreferencesHelper(context: Context) {

    private var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    fun putString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun readString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun putBoolean(key: String, value: Boolean?) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value!!)
        editor.apply()
    }

    fun readBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun readBoolean(key: String, defValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defValue)
    }

}
