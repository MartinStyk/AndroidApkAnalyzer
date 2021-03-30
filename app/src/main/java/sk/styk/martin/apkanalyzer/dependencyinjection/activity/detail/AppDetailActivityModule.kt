package sk.styk.martin.apkanalyzer.dependencyinjection.activity.detail

import android.app.Activity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import sk.styk.martin.apkanalyzer.dependencyinjection.activity.ActivityCommonModule
import sk.styk.martin.apkanalyzer.dependencyinjection.util.FragmentScope
import sk.styk.martin.apkanalyzer.dependencyinjection.viewmodel.ViewModelFactoriesModule
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.base.AppDetailActivity
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.base.AppDetailFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.activity.AppActivityDetailFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.certificate.AppCertificateDetailFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.feature.AppFeatureDetailPageFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.general.AppGeneralDetailFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.provider.AppProviderDetailFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.receiver.AppReceiverDetailFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.resource.AppResourceDetailFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.service.AppServiceDetailFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.usedpermission.AppDefinedPermissionDetailFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.usedpermission.AppUsedPermissionDetailFragment

@Module(includes = [
    ActivityCommonModule::class,
    ViewModelFactoriesModule::class,
])
abstract class AppDetailActivityModule {

    @Binds
    abstract fun activity(activity: AppDetailActivity): Activity

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun appDetailFragmentInjector(): AppDetailFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun generalDetailFragmentInjector(): AppGeneralDetailFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun appCertificateDetailFragmentInjector(): AppCertificateDetailFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun appUsedPermissionDetailFragmentInjector(): AppUsedPermissionDetailFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun appActivityDetailFragmentInjector(): AppActivityDetailFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun appServiceDetailFragmentInjector(): AppServiceDetailFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun appProviderDetailFragmentInjector(): AppProviderDetailFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun appReceiverDetailFragmentInjector(): AppReceiverDetailFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun appFeatureDetailFragmentInjector(): AppFeatureDetailPageFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun appDefinedPermissionDetailFragmentInjector(): AppDefinedPermissionDetailFragment

    @FragmentScope
    @ContributesAndroidInjector
    abstract fun appResourceDetailFragmentInjector(): AppResourceDetailFragment

}