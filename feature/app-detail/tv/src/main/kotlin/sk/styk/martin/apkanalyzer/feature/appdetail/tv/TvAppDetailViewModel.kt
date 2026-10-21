package sk.styk.martin.apkanalyzer.feature.appdetail.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import sk.styk.martin.apkanalyzer.core.apps.AppDetailRepository
import sk.styk.martin.apkanalyzer.core.common.model.AppReference
import sk.styk.martin.apkanalyzer.core.common.model.PackageName
import sk.styk.martin.apkanalyzer.core.userpreferences.recentlyviewed.RecentlyViewedAppsRepository
import sk.styk.martin.apkanalyzer.feature.appdetail.api.AppDetailInput

@HiltViewModel(assistedFactory = TvAppDetailViewModel.Factory::class)
internal class TvAppDetailViewModel @AssistedInject constructor(
    @Assisted appDetailInput: AppDetailInput,
    appDetailRepository: AppDetailRepository,
    recentlyViewedAppsRepository: RecentlyViewedAppsRepository,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(appDetailInput: AppDetailInput): TvAppDetailViewModel
    }

    private val mutableState = MutableStateFlow<TvAppDetailState>(TvAppDetailState.Loading)
    val state: StateFlow<TvAppDetailState> = mutableState.asStateFlow()

    init {
        val appReference = appDetailInput.toAppReference()
        viewModelScope.launch {
            mutableState.value = appDetailRepository.details(appReference).fold(
                onSuccess = { detail ->
                    TvAppDetailState.Loaded(
                        appReference = appReference,
                        appName = detail.info.applicationName,
                        packageName = detail.info.packageName.value,
                        versionName = detail.info.versionName,
                    )
                },
                onFailure = { TvAppDetailState.Error },
            )
            if (mutableState.value is TvAppDetailState.Loaded && appReference is AppReference.InstalledPackage) {
                recentlyViewedAppsRepository.addRecent(appReference.packageName)
            }
        }
    }
}

private fun AppDetailInput.toAppReference(): AppReference = when (this) {
    is AppDetailInput.InstalledPackage -> AppReference.InstalledPackage(PackageName(packageName))
    is AppDetailInput.ApkFile -> AppReference.ApkFile(apkFilePath)
}
