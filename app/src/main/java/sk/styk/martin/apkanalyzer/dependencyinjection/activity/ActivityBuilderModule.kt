package sk.styk.martin.apkanalyzer.dependencyinjection.activity

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.styk.martin.apkanalyzer.dependencyinjection.activity.main.MainActivityModule
import sk.styk.martin.apkanalyzer.dependencyinjection.activity.permission.PermissionDetailActivityModule
import sk.styk.martin.apkanalyzer.dependencyinjection.util.ActivityScope
import sk.styk.martin.apkanalyzer.ui.activity.mainactivity.MainActivity
import sk.styk.martin.apkanalyzer.ui.activity.permission.detail.PermissionDetailActivity

@Module
abstract class ActivityBuilderModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun mainActivityInjector(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [PermissionDetailActivityModule::class])
    abstract fun permissionDetailActivityInjector(): PermissionDetailActivity

}