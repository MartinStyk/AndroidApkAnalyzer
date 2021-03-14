package sk.styk.martin.apkanalyzer.dependencyinjection.viewmodel

import com.squareup.inject.assisted.dagger2.AssistedModule
import dagger.Module

@AssistedModule
@Module(includes = [
    AssistedInject_AppViewModelFactoriesModule::class,
]
)
abstract class AppViewModelFactoriesModule