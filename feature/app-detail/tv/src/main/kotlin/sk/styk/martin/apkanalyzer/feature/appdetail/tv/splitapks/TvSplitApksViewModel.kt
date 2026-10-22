package sk.styk.martin.apkanalyzer.feature.appdetail.tv.splitapks

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
import sk.styk.martin.apkanalyzer.core.common.coroutines.DispatcherProvider
import sk.styk.martin.apkanalyzer.feature.appdetail.api.AppDetailInput
import sk.styk.martin.apkanalyzer.feature.appdetail.tv.toAppReference

@HiltViewModel(assistedFactory = TvSplitApksViewModel.Factory::class)
internal class TvSplitApksViewModel @AssistedInject constructor(
    @Assisted private val appDetailInput: AppDetailInput,
    private val appDetailRepository: AppDetailRepository,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    @AssistedFactory
    interface Factory {
        fun create(appDetailInput: AppDetailInput): TvSplitApksViewModel
    }

    private val mutableState = MutableStateFlow<TvSplitApksState>(TvSplitApksState.Loading)
    val state: StateFlow<TvSplitApksState> = mutableState.asStateFlow()

    init {
        loadSplits()
    }

    fun onAction(action: TvSplitApksAction) {
        when (action) {
            TvSplitApksAction.Retry -> loadSplits()
        }
    }

    private fun loadSplits() {
        mutableState.value = TvSplitApksState.Loading
        viewModelScope.launch(dispatcherProvider.default()) {
            mutableState.value = appDetailRepository.details(appDetailInput.toAppReference()).fold(
                onSuccess = { detail ->
                    TvSplitApksState.Loaded(
                        appName = detail.info.applicationName,
                        splits = detail.info.installedSplits.sortedWith(compareBy({ it.kind }, { it.qualifier })),
                    )
                },
                onFailure = { TvSplitApksState.Error },
            )
        }
    }
}
