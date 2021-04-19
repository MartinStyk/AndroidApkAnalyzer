package sk.styk.martin.apkanalyzer.dependencyinjection.app

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module(includes = [
    ApplicationCommonModule::class,
])
abstract class ApplicationModule