package sk.styk.martin.apkanalyzer.dependencyinjection.util

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ApplicationScope

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ActivityScope

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FragmentScope