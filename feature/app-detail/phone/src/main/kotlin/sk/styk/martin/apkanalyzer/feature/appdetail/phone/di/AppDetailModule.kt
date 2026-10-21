package sk.styk.martin.apkanalyzer.feature.appdetail.phone.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import sk.styk.martin.apkanalyzer.feature.appdetail.phone.AppSummaryTextFormatter
import sk.styk.martin.apkanalyzer.feature.appdetail.phone.AppSummaryTextFormatterImpl

@Module
@InstallIn(SingletonComponent::class)
internal interface AppDetailModule {
    @Binds
    fun bindAppSummaryTextFormatter(impl: AppSummaryTextFormatterImpl): AppSummaryTextFormatter
}
