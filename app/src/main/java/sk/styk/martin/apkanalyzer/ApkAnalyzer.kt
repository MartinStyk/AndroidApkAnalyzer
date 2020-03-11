package sk.styk.martin.apkanalyzer

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.google.android.gms.ads.MobileAds
import sk.styk.martin.apkanalyzer.util.ColorThemeHelper

class ApkAnalyzer : MultiDexApplication() {

    override fun onCreate() {
        instance = this

        ColorThemeHelper.setTheme(this)

        super.onCreate()

        if (BuildConfig.SHOW_ADS) {
            MobileAds.initialize(this, getString(R.string.ad_mod_app_id))
        }
    }

    companion object {

        private lateinit var instance: ApkAnalyzer

        val context: Context
            get() = instance.applicationContext
    }
}