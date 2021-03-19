package sk.styk.martin.apkanalyzer.dependencyinjection.viewmodel

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sk.styk.martin.apkanalyzer.ui.about.AboutFragmentViewModel
import sk.styk.martin.apkanalyzer.ui.activity.applist.searchable.AppListViewModel
import sk.styk.martin.apkanalyzer.ui.activity.mainactivity.MainActivityViewModel
import sk.styk.martin.apkanalyzer.ui.permission.list.PermissionListFragmentViewModel

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
    @ViewModelKey(value = AppListViewModel::class)
    abstract fun bindAppListViewModel(viewModel: AppListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = AboutFragmentViewModel::class)
    abstract fun bindAboutFragmentViewModel(viewModel: AboutFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(value = PermissionListFragmentViewModel::class)
    abstract fun bindPermissionListFragmentViewModell(viewModel: PermissionListFragmentViewModel): ViewModel

}