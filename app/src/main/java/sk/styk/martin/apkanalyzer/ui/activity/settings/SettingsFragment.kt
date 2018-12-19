package sk.styk.martin.apkanalyzer.ui.activity.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import android.view.Menu
import android.view.MenuInflater
import com.google.firebase.analytics.FirebaseAnalytics
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.util.ColorThemeHelper

/**
 * @author Martin Styk
 */
class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onStart() {
        super.onStart()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        FirebaseAnalytics.getInstance(requireContext()).setCurrentScreen(requireActivity(), SettingsFragment::class.java.simpleName, SettingsFragment::class.java.simpleName)
    }

    override fun onStop() {
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onStop()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == getString(R.string.preference_color_theme_key))
            ColorThemeHelper.setTheme(requireContext())
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // Hide action bar item for searching
        menu?.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }
}
