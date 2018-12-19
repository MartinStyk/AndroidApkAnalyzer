package sk.styk.martin.apkanalyzer.util

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatDelegate
import sk.styk.martin.apkanalyzer.R

/**
 * @author Martin Styk {@literal <martin.styk@gmail.com>}
 */

const val DEFAULT_THEME = "light"

object ColorThemeHelper {

    fun setTheme(context: Context) {
        val preference = SharedPreferencesHelper(context).readString(context.getString(R.string.preference_color_theme_key))
                ?: DEFAULT_THEME
        when (preference) {
            "light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            "dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        if (context is Activity)
            context.recreate()
    }

}