package sk.styk.martin.apkanalyzer.dependencyinjection.activity

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.styk.martin.apkanalyzer.dependencyinjection.activity.main.MainActivityModule
import sk.styk.martin.apkanalyzer.ui.activity.MainActivity

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun mainActivityInjector(): MainActivity

}