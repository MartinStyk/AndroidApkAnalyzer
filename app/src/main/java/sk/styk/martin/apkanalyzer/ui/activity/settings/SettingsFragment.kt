package sk.styk.martin.apkanalyzer.ui.activity.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.preference.PreferenceFragmentCompat
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.android.support.AndroidSupportInjection
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.dependencyinjection.util.ForApplication
import sk.styk.martin.apkanalyzer.manager.resources.ColorThemeManager
import javax.inject.Inject

class SettingsFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    @ForApplication
    lateinit var colorThemeManager: ColorThemeManager

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings, rootKey)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        FirebaseAnalytics.getInstance(requireContext()).setCurrentScreen(requireActivity(), SettingsFragment::class.java.simpleName, SettingsFragment::class.java.simpleName)
    }

    override fun onStop() {
        preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        super.onStop()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        colorThemeManager.setTheme()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Hide action bar item for searching
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }
}
