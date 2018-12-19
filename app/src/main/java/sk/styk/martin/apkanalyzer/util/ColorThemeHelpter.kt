package sk.styk.martin.apkanalyzer.util

import android.app.Activity
import android.content.Context
import android.support.v7.app.AppCompatDelegate
import sk.styk.martin.apkanalyzer.R

/**
 * @author Martin Styk {@literal <martin.styk@gmail.com>}
 */

const val DARK_THEME = "dark"
const val LIGHT_THEME = "light"
const val DEFAULT_THEME = LIGHT_THEME

object ColorThemeHelper {

    fun setTheme(context: Context) {
        val preference = SharedPreferencesHelper(context).readString(context.getString(R.string.preference_color_theme_key)) ?: DEFAULT_THEME

        when (preference) {
            LIGHT_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            DARK_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        (context as? Activity)?.recreate()
    }

    fun setTheme(color: String, context: Context) {

        when (color) {
            LIGHT_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            DARK_THEME -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else -> return
        }

        SharedPreferencesHelper(context).putString(context.getString(R.string.preference_color_theme_key), color)
        (context as? Activity)?.recreate()
    }

}