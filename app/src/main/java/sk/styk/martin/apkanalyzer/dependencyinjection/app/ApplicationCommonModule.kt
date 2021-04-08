package sk.styk.martin.apkanalyzer.dependencyinjection.app

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule
import sk.styk.martin.apkanalyzer.dependencyinjection.activity.ActivityBuilderModule
import sk.styk.martin.apkanalyzer.dependencyinjection.util.ForApplication
import sk.styk.martin.apkanalyzer.manager.resources.ColorThemeManager
import sk.styk.martin.apkanalyzer.manager.resources.ColorThemeManagerImpl
import javax.inject.Singleton

@Module(includes = [
    AndroidSupportInjectionModule::class,
    ActivityBuilderModule::class,
])
class ApplicationCommonModule {

    @Provides
    @Singleton
    @ForApplication
    fun provideApplicationContext(application: Application): Context = application.applicationContext

    @Provides
    @Singleton
    @ForApplication
    fun providesResources(application: Application): Resources = application.resources

    @Provides
    @Singleton
    fun providePackageManager(application: Application): PackageManager = application.packageManager

    @Provides
    @Singleton
    fun providePersistenceSharedPreferences(@ForApplication context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    @Provides
    fun provideNotificationManager(@ForApplication context: Context): NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Provides
    @ForApplication
    fun provideColorThemeManagerImpl(colorThemeManagerImpl: ColorThemeManagerImpl): ColorThemeManager = colorThemeManagerImpl

}