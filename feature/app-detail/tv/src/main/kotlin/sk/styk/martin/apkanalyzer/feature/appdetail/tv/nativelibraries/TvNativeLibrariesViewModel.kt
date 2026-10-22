package sk.styk.martin.apkanalyzer.feature.appdetail.tv.nativelibraries

import android.os.Build
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
import sk.styk.martin.apkanalyzer.core.apps.packaging.NativeLibrariesRepository
import sk.styk.martin.apkanalyzer.core.apps.packaging.groupedByLibrary
import sk.styk.martin.apkanalyzer.core.common.coroutines.DispatcherProvider
import sk.styk.martin.apkanalyzer.feature.appdetail.api.AppDetailInput
import sk.styk.martin.apkanalyzer.feature.appdetail.tv.toAppReference

@HiltViewModel(assistedFactory = TvNativeLibrariesViewModel.Factory::class)
internal class TvNativeLibrariesViewModel @AssistedInject constructor(
    @Assisted private val appDetailInput: AppDetailInput,
    private val appDetailRepository: AppDetailRepository,
    private val nativeLibrariesRepository: NativeLibrariesRepository,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(appDetailInput: AppDetailInput): TvNativeLibrariesViewModel
    }

    private val mutableState = MutableStateFlow<TvNativeLibrariesState>(TvNativeLibrariesState.Loading)
    val state: StateFlow<TvNativeLibrariesState> = mutableState.asStateFlow()

    init {
        loadLibraries()
    }

    fun onAction(action: TvNativeLibrariesAction) {
        when (action) {
            TvNativeLibrariesAction.Retry -> loadLibraries()
        }
    }

    private fun loadLibraries() {
        mutableState.value = TvNativeLibrariesState.Loading
        viewModelScope.launch(dispatcherProvider.default()) {
            val reference = appDetailInput.toAppReference()
            val appName = appDetailRepository.details(reference).getOrNull()?.info?.applicationName.orEmpty()
            mutableState.value = nativeLibrariesRepository.nativeLibraries(reference).fold(
                onSuccess = { libraries ->
                    TvNativeLibrariesState.Loaded(
                        appName = appName,
                        items = libraries.files.groupedByLibrary(Build.SUPPORTED_ABIS.toSet()),
                        deviceAbis = Build.SUPPORTED_ABIS.toList(),
                    )
                },
                onFailure = { TvNativeLibrariesState.Error },
            )
        }
    }
}
