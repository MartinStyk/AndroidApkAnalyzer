package sk.styk.martin.apkanalyzer

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.google.android.gms.ads.MobileAds
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import sk.styk.martin.apkanalyzer.dependencyinjection.app.ApplicationComponent
import sk.styk.martin.apkanalyzer.dependencyinjection.app.DaggerApplicationComponent
import sk.styk.martin.apkanalyzer.util.ColorThemeHelper
import javax.inject.Inject

class ApkAnalyzer : MultiDexApplication(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    lateinit var appComponent: ApplicationComponent
        private set

    override fun onCreate() {
        appComponent = DaggerApplicationComponent.builder().create(this) as ApplicationComponent
        appComponent.inject(this)

        instance = this

        ColorThemeHelper.setTheme(this)

        super.onCreate()

        if (BuildConfig.SHOW_ADS) {
            MobileAds.initialize(this, getString(R.string.ad_mod_app_id))
        }
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    // TODO remove
    companion object {

        private lateinit var instance: ApkAnalyzer

        val context: Context
            get() = instance.applicationContext
    }
}