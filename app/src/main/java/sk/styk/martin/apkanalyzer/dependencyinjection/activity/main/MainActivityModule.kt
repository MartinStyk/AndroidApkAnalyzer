package sk.styk.martin.apkanalyzer.dependencyinjection.activity.main

import android.app.Activity
import dagger.Binds
import dagger.Module
import sk.styk.martin.apkanalyzer.dependencyinjection.activity.ActivityCommonModule
import sk.styk.martin.apkanalyzer.dependencyinjection.viewmodel.ViewModelFactoriesModule
import sk.styk.martin.apkanalyzer.ui.activity.MainActivity

@Module(includes = [
    ActivityCommonModule::class,
    ViewModelFactoriesModule::class,
])
abstract class MainActivityModule {

    @Binds
    abstract fun activity(activity: MainActivity): Activity

}