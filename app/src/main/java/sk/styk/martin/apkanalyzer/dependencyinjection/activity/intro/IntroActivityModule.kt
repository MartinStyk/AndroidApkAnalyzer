package sk.styk.martin.apkanalyzer.dependencyinjection.activity.intro

import android.app.Activity
import dagger.Binds
import dagger.Module
import sk.styk.martin.apkanalyzer.dependencyinjection.activity.ActivityCommonModule
import sk.styk.martin.apkanalyzer.dependencyinjection.viewmodel.ViewModelFactoriesModule
import sk.styk.martin.apkanalyzer.ui.intro.IntroActivity

@Module(includes = [
    ActivityCommonModule::class,
    ViewModelFactoriesModule::class,
])
abstract class IntroActivityModule {

    @Binds
    abstract fun activity(activity: IntroActivity): Activity

}