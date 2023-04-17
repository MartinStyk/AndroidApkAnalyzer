package sk.styk.martin.apkanalyzer.manager.resources

import androidx.appcompat.app.AppCompatDelegate
import sk.styk.martin.apkanalyzer.manager.persistence.SettingsManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ColorThemeManagerImpl @Inject constructor(private val settingsManager: SettingsManager) : ColorThemeManager {

    override fun setTheme() = when (settingsManager.colorScheme) {
        SettingsManager.ColorScheme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        SettingsManager.ColorScheme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        SettingsManager.ColorScheme.DEFAULT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}
