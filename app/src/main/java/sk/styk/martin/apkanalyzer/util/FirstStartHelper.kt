package sk.styk.martin.apkanalyzer.util

import android.content.Context

/**
 * @author Martin Styk
 * @version 15.11.2017.
 */
object FirstStartHelper {

    private const val FIRST_APP_START = "first_app_start"

    fun isFirstStart(context: Context): Boolean {
        return SharedPreferencesHelper(context).readBoolean(FIRST_APP_START, true)
    }

    fun setFirstStartFinished(context: Context) {
        SharedPreferencesHelper(context).putBoolean(FIRST_APP_START, false)
    }
}
