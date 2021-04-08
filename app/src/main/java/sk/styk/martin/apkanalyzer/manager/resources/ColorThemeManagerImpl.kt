package sk.styk.martin.apkanalyzer.manager.resources

import androidx.appcompat.app.AppCompatDelegate
import sk.styk.martin.apkanalyzer.dependencyinjection.util.ForApplication
import sk.styk.martin.apkanalyzer.manager.persistence.SettingsManager
import sk.styk.martin.apkanalyzer.util.coroutines.DispatcherProvider
import javax.inject.Inject
import javax.inject.Singleton

@ForApplication
@Singleton
class ColorThemeManagerImpl @Inject constructor(private val settingsManager: SettingsManager,
                                                private val dispatcherProvider: DispatcherProvider) : ColorThemeManager {

    override fun setTheme() = when (settingsManager.colorScheme) {
        SettingsManager.ColorScheme.LIGHT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        SettingsManager.ColorScheme.DARK -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        SettingsManager.ColorScheme.DEFAULT -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }

}