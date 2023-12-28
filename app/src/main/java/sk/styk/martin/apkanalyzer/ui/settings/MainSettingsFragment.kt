package sk.styk.martin.apkanalyzer.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.manager.persistence.DAY_NIGHT_KEY
import sk.styk.martin.apkanalyzer.manager.persistence.SettingsManager
import sk.styk.martin.apkanalyzer.manager.resources.ColorThemeManager
import javax.inject.Inject

@AndroidEntryPoint
class MainSettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    lateinit var settingsManager: SettingsManager

    @Inject
    lateinit var colorThemeManager: ColorThemeManager

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onStart() {
        super.onStart()
        preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
        (findPreference(DAY_NIGHT_KEY) as? ListPreference)?.setDefaultValue(settingsManager.colorScheme.persist())
    }

    override fun onStop() {
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
        super.onStop()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        colorThemeManager.setTheme()
    }
}
