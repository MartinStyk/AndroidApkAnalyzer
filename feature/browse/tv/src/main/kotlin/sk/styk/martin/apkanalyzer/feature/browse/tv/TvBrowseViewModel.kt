package sk.styk.martin.apkanalyzer.feature.browse.tv

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import sk.styk.martin.apkanalyzer.core.appindex.AppIndexRepository
import sk.styk.martin.apkanalyzer.core.appindex.browse.BrowseDimensionLabeler
import sk.styk.martin.apkanalyzer.core.appindex.browse.dimensionSummaries
import sk.styk.martin.apkanalyzer.core.appindex.model.AppIndexStatus
import sk.styk.martin.apkanalyzer.core.apps.InstalledAppsRepository
import sk.styk.martin.apkanalyzer.core.common.coroutines.DispatcherProvider
import javax.inject.Inject

@HiltViewModel
internal class TvBrowseViewModel @Inject constructor(
    appIndexRepository: AppIndexRepository,
    installedAppsRepository: InstalledAppsRepository,
    private val labeler: BrowseDimensionLabeler,
    dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    val state: StateFlow<TvBrowseState> = combine(appIndexRepository.index(), installedAppsRepository.apps()) { status, apps ->
        when (status) {
            AppIndexStatus.Loading -> TvBrowseState.Loading

            is AppIndexStatus.Data -> TvBrowseState.Loaded(
                totalApps = apps.size,
                dimensions = status.index.dimensionSummaries(labeler, TOP_LABELS_COUNT),
            )
        }
    }
        .flowOn(dispatcherProvider.io())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TvBrowseState.Loading)

    private val eventChannel = Channel<TvBrowseEvent>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    fun onAction(action: TvBrowseAction) {
        when (action) {
            is TvBrowseAction.SelectDimension -> eventChannel.trySend(TvBrowseEvent.NavigateToDimension(action.dimension))
        }
    }
}

private const val TOP_LABELS_COUNT = 4
