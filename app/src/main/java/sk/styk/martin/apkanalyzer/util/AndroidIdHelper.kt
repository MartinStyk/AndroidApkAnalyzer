package sk.styk.martin.apkanalyzer.util

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings
import java.util.*

object AndroidIdHelper {

    const val ANDROID_ID = "android_id"

    @SuppressLint("HardwareIds")
    fun getAndroidId(context: Context): String {
        val androidId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

        if (androidId != null)
            return androidId

        // if android id is not found, look for generated value in shared preferences
        val sharedPreferencesHelper = SharedPreferencesHelper(context)
        val fromPreferences = sharedPreferencesHelper.readString(ANDROID_ID)

        if (fromPreferences != null)
            return fromPreferences

        // if no value is found in preferences, generate and save it
        val generatedId = UUID.randomUUID().toString()
        sharedPreferencesHelper.putString(ANDROID_ID, generatedId)

        return generatedId
    }
}
