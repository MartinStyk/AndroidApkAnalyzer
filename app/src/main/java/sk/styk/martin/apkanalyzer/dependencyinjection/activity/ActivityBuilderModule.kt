package sk.styk.martin.apkanalyzer.dependencyinjection.activity

import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.styk.martin.apkanalyzer.dependencyinjection.activity.detail.AppDetailActivityModule
import sk.styk.martin.apkanalyzer.dependencyinjection.activity.detail.OnInstallAppDetailActivityModule
import sk.styk.martin.apkanalyzer.dependencyinjection.activity.main.MainActivityModule
import sk.styk.martin.apkanalyzer.dependencyinjection.activity.permission.PermissionDetailActivityModule
import sk.styk.martin.apkanalyzer.dependencyinjection.util.ActivityScope
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailActivity
import sk.styk.martin.apkanalyzer.ui.appdetail.OnInstallAppDetailActivity
import sk.styk.martin.apkanalyzer.ui.main.MainActivity
import sk.styk.martin.apkanalyzer.ui.permission.detail.PermissionDetailActivity

@Module
abstract class ActivityBuilderModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun mainActivityInjector(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [PermissionDetailActivityModule::class])
    abstract fun permissionDetailActivityInjector(): PermissionDetailActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [AppDetailActivityModule::class])
    abstract fun appDetailActivityInjector(): AppDetailActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [OnInstallAppDetailActivityModule::class])
    abstract fun onInstallAppDetailActivityInjector(): OnInstallAppDetailActivity

}