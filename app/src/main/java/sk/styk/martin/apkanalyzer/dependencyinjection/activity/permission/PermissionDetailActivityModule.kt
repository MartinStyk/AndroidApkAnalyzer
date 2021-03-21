package sk.styk.martin.apkanalyzer.dependencyinjection.activity.permission

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.styk.martin.apkanalyzer.dependencyinjection.activity.ActivityCommonModule
import sk.styk.martin.apkanalyzer.dependencyinjection.util.FragmentScope
import sk.styk.martin.apkanalyzer.dependencyinjection.viewmodel.ViewModelFactoriesModule
import sk.styk.martin.apkanalyzer.ui.permission.detail.PermissionDetailActivity
import sk.styk.martin.apkanalyzer.ui.permission.detail.apps.PermissionsAppListFragment
import sk.styk.martin.apkanalyzer.ui.permission.detail.details.PermissionsGeneralDetailsFragment
import sk.styk.martin.apkanalyzer.ui.permission.detail.pager.PermissionDetailFragment

@Module(includes = [
    ActivityCommonModule::class,
    ViewModelFactoriesModule::class,
])
abstract class PermissionDetailActivityModule {

    @Binds
    abstract fun activity(activity: PermissionDetailActivity): Activity

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun permissionDetailPagerFragmentInjector(): PermissionDetailFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun permissionsGeneralDetailsFragmentInjector(): PermissionsGeneralDetailsFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun permissionsAppListFragmentInjector(): PermissionsAppListFragment

}