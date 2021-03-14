package sk.styk.martin.apkanalyzer.dependencyinjection.viewmodel

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import sk.styk.martin.apkanalyzer.ui.activity.MainActivityViewModel

@Module(includes = [
    AppViewModelFactoriesModule::class,
])
abstract class ViewModelFactoriesModule {

    @Binds
    @IntoMap
    @ViewModelKey(value = MainActivityViewModel::class)
    abstract fun bindMainActivityViewModel(viewModel: MainActivityViewModel): ViewModel

}