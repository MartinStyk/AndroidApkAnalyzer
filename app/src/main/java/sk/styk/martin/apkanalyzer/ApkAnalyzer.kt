package sk.styk.martin.apkanalyzer

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import sk.styk.martin.apkanalyzer.dependencyinjection.util.ForApplication
import sk.styk.martin.apkanalyzer.manager.persistence.PersistenceManager
import sk.styk.martin.apkanalyzer.manager.resources.ColorThemeManager
import sk.styk.martin.apkanalyzer.util.LogUtils
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ApkAnalyzer : MultiDexApplication() {

    @Inject
    @ForApplication
    lateinit var colorThemeManager: ColorThemeManager

    @Inject
    lateinit var persistenceManager: PersistenceManager

    override fun onCreate() {
        super.onCreate()
        colorThemeManager.setTheme()
        Timber.plant(*LogUtils.logTrees())
    }

}