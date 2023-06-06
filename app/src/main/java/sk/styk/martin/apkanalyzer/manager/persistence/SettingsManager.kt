package sk.styk.martin.apkanalyzer.manager.persistence

import android.content.SharedPreferences
import javax.inject.Inject

private const val DAY_NIGHT_KEY = "dayNightPref"

class SettingsManager @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) {

    enum class ColorScheme(private val persisted: String) {
        LIGHT("light"),
        DARK("dark"),
        DEFAULT("system");

        fun persist() = persisted

        companion object {
            fun from(persisted: String) = values()
                .firstOrNull { it.persisted == persisted }
                ?: DEFAULT
        }
    }

    var colorScheme: ColorScheme
        get() = sharedPreferences.getString(DAY_NIGHT_KEY, null)?.let { ColorScheme.from(it) }
            ?: ColorScheme.DEFAULT
        set(value) = sharedPreferences.edit().putString(DAY_NIGHT_KEY, value.persist()).apply()
}
