package sk.styk.martin.apkanalyzer.dependencyinjection.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule
import sk.styk.martin.apkanalyzer.dependencyinjection.activity.ActivityBuilderModule
import sk.styk.martin.apkanalyzer.dependencyinjection.util.ApplicationScope
import javax.inject.Singleton

@Module(includes = [
    AndroidSupportInjectionModule::class,
    ActivityBuilderModule::class,
])
class ApplicationCommonModule {

    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationContext(application: Application): Context = application.applicationContext

    @Provides
    @Singleton
    @ApplicationScope
    fun providesResources(application: Application): Resources = application.resources

    @Provides
    @Singleton
    @ApplicationScope
    fun providePackageManager(application: Application): PackageManager = application.packageManager

    @Provides
    @Singleton
    fun providePersistenceSharedPreferences(@ApplicationScope context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

}