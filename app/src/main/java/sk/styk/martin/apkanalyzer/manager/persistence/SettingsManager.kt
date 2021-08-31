package sk.styk.martin.apkanalyzer.manager.persistence

import android.content.SharedPreferences
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.manager.resources.ResourcesManager
import javax.inject.Inject

class SettingsManager @Inject constructor(
        private val sharedPreferences: SharedPreferences,
        private val resourcesManager: ResourcesManager,
) {

    enum class ColorScheme(private val persisted: String) {
        LIGHT("light"),
        DARK("dark"),
        DEFAULT("system");

        fun persist() = persisted

        companion object {
            fun from(persisted: String) = values().firstOrNull { it.persisted == persisted }
                    ?: DEFAULT
        }
    }

    var colorScheme: ColorScheme
        get() = sharedPreferences.getString(resourcesManager.getString(R.string.preference_color_theme_key).toString(), null)?.let { ColorScheme.from(it) }
                ?: ColorScheme.DEFAULT
        set(value) = sharedPreferences.edit().putString(resourcesManager.getString(R.string.preference_color_theme_key).toString(), value.persist()).apply()
}



