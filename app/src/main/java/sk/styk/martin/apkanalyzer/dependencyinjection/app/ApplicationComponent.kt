package sk.styk.martin.apkanalyzer.dependencyinjection.app

import dagger.Component
import dagger.android.AndroidInjector
import sk.styk.martin.apkanalyzer.ApkAnalyzer
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent : AndroidInjector<ApkAnalyzer> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<ApkAnalyzer>()

}