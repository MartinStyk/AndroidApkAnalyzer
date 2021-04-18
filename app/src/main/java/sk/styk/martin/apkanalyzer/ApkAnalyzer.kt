package sk.styk.martin.apkanalyzer

import androidx.multidex.MultiDexApplication
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import sk.styk.martin.apkanalyzer.dependencyinjection.app.ApplicationComponent
import sk.styk.martin.apkanalyzer.dependencyinjection.app.DaggerApplicationComponent
import sk.styk.martin.apkanalyzer.dependencyinjection.util.ForApplication
import sk.styk.martin.apkanalyzer.manager.resources.ColorThemeManager
import javax.inject.Inject

class ApkAnalyzer : MultiDexApplication(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    @ForApplication
    lateinit var colorThemeManager: ColorThemeManager

    lateinit var appComponent: ApplicationComponent
        private set

    override fun onCreate() {
        appComponent = DaggerApplicationComponent.builder().create(this) as ApplicationComponent
        appComponent.inject(this)

        colorThemeManager.setTheme()

        super.onCreate()
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

}