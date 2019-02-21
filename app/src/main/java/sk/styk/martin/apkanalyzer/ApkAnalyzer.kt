package sk.styk.martin.apkanalyzer

import android.app.Application
import android.content.Context
import android.os.StrictMode
import com.google.android.gms.ads.MobileAds
import com.squareup.leakcanary.LeakCanary
import sk.styk.martin.apkanalyzer.util.AppFlavour
import sk.styk.martin.apkanalyzer.util.ColorThemeHelper

/**
 * @author Martin Styk
 * @version 30.10.2017.
 */
class ApkAnalyzer : Application() {

    override fun onCreate() {
        instance = this

        ColorThemeHelper.setTheme(this)

        super.onCreate()

        if (LeakCanary.isInAnalyzerProcess(this))
            return
        LeakCanary.install(this)

        // Initialize the Mobile Ads SDK.
        if (!AppFlavour.isPremium)
            MobileAds.initialize(this, getString(R.string.ad_mod_app_id))

        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
    }

    companion object {

        private lateinit var instance: ApkAnalyzer

        val context: Context
            get() = instance.applicationContext
    }
}