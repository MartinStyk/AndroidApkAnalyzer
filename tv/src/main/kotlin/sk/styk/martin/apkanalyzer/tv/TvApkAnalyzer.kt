package sk.styk.martin.apkanalyzer.tv

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import sk.styk.martin.apkanalyzer.core.common.logger.Logger

@HiltAndroidApp
class TvApkAnalyzer : Application() {

    override fun onCreate() {
        super.onCreate()
        Logger.init(logToConsole = BuildConfig.DEBUG)
    }
}
