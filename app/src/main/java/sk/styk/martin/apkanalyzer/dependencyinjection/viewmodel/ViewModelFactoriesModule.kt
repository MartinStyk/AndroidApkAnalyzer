package sk.styk.martin.apkanalyzer.dependencyinjection.viewmodel

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sk.styk.martin.apkanalyzer.ui.about.AboutFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.activity.localstatistics.LocalStatisticsFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.applist.main.MainAppListViewModel
import sk.styk.martin.apkanalyzer.ui.main.MainActivityViewModel
import sk.styk.martin.apkanalyzer.ui.permission.list.PermissionListFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.premium.PremiumFragmentViewModel

@Module(includes = [
    AppViewModelFactoriesModule::class,
])
abstract class ViewModelFactoriesModule {

    @Binds
    @IntoMap
    @ViewModelKey(value = MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = MainAppListViewModel::class)
    abstract fun bindAppListViewModel(viewModel: MainAppListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = AboutFragmentViewModel::class)
    abstract fun bindAboutFragmentViewModel(viewModel: AboutFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = PermissionListFragmentViewModel::class)
    abstract fun bindPermissionListFragmentViewModel(viewModel: PermissionListFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = PremiumFragmentViewModel::class)
    abstract fun bindPremiumFragmentViewModel(viewModel: PremiumFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = LocalStatisticsFragmentViewModel::class)
    abstract fun bindLocalStatisticsFragmentViewModel(viewModel: LocalStatisticsFragmentViewModel): ViewModel

}