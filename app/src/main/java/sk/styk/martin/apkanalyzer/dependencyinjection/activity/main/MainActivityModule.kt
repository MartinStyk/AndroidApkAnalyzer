package sk.styk.martin.apkanalyzer.dependencyinjection.activity.main

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import sk.styk.martin.apkanalyzer.dependencyinjection.activity.ActivityCommonModule

@InstallIn(ActivityComponent::class)
@Module(includes = [
    ActivityCommonModule::class,
])
abstract class MainActivityModule