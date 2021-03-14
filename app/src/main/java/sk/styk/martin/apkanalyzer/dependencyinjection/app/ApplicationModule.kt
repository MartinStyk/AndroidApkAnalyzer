package sk.styk.martin.apkanalyzer.dependencyinjection.app

import android.app.Application
import dagger.Binds
import dagger.Module
import sk.styk.martin.apkanalyzer.ApkAnalyzer
import javax.inject.Singleton

@Module(includes = [
    ApplicationCommonModule::class,
])
abstract class ApplicationModule {

    @Singleton
    @Binds
    abstract fun application(app: ApkAnalyzer): Application

}