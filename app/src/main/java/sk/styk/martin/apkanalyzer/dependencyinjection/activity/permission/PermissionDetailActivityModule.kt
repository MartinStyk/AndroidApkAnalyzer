package sk.styk.martin.apkanalyzer.dependencyinjection.activity.permission

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.styk.martin.apkanalyzer.dependencyinjection.activity.ActivityCommonModule
import sk.styk.martin.apkanalyzer.dependencyinjection.util.FragmentScope
import sk.styk.martin.apkanalyzer.dependencyinjection.viewmodel.ViewModelFactoriesModule
import sk.styk.martin.apkanalyzer.ui.activity.permission.detail.PermissionDetailActivity
import sk.styk.martin.apkanalyzer.ui.activity.permission.detail.pager.PermissionDetailPagerFragment

@Module(includes = [
    ActivityCommonModule::class,
    ViewModelFactoriesModule::class,
])
abstract class PermissionDetailActivityModule {

    @Binds
    abstract fun activity(activity: PermissionDetailActivity): Activity

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun permissionDetailPagerFragmentInjector(): PermissionDetailPagerFragment

}