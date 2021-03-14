package sk.styk.martin.apkanalyzer.dependencyinjection.activity

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import sk.styk.martin.apkanalyzer.dependencyinjection.util.ActivityScope
import sk.styk.martin.apkanalyzer.dependencyinjection.util.ApplicationScope
import javax.inject.Singleton

@Module
class ActivityCommonModule {

    @Provides
    @ActivityScope
    fun provideActivityContext(activity: Activity): Context = activity.baseContext

    @Provides
    @ActivityScope
    fun providesResources(activity: Activity): Activity = activity

}