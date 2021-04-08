package sk.styk.martin.apkanalyzer.manager.persistence

import android.content.SharedPreferences
import javax.inject.Inject

private const val FIRST_APP_START = "first_app_start"
private const val PROMO_SHOW_TIME = "promo_shown"
private const val NEW_FEATURE_SHOW = "new_feature_shown"


class PersistenceManager @Inject constructor(
        private val sharedPreferences: SharedPreferences
) {

    var isFirstStart: Boolean
        get() = sharedPreferences.getBoolean(FIRST_APP_START, true)
        set(value) = sharedPreferences.edit().putBoolean(FIRST_APP_START, value).apply()

    var lastPromoShowTime: Long
        get() = sharedPreferences.getLong(PROMO_SHOW_TIME, -1)
        set(value) = sharedPreferences.edit().putLong(PROMO_SHOW_TIME, value).apply()

    var newFeatureShowVersion: Long
        get() = sharedPreferences.getLong(NEW_FEATURE_SHOW, -1)
        set(value) = sharedPreferences.edit().putLong(NEW_FEATURE_SHOW, value).apply()

}