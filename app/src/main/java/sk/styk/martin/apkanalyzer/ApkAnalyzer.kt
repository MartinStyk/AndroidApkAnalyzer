package sk.styk.martin.apkanalyzer

import androidx.multidex.MultiDexApplication
import dagger.hilt.android.HiltAndroidApp
import sk.styk.martin.apkanalyzer.manager.resources.ColorThemeManager
import sk.styk.martin.apkanalyzer.util.LogUtils
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class ApkAnalyzer : MultiDexApplication() {

    @Inject
    lateinit var colorThemeManager: ColorThemeManager

    override fun onCreate() {
        super.onCreate()
        colorThemeManager.setTheme()
        Timber.plant(*LogUtils.logTrees())
    }
}
