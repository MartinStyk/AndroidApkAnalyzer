package sk.styk.martin.apkanalyzer.manager.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import sk.styk.martin.apkanalyzer.manager.navigationdrawer.ForegroundFragmentWatcher
import sk.styk.martin.apkanalyzer.util.FragmentTag
import sk.styk.martin.apkanalyzer.core.common.coroutines.DispatcherProvider
import javax.inject.Inject

@ActivityScoped
class FragmentScreenTracker @Inject constructor(
    private val foregroundFragmentWatcher: ForegroundFragmentWatcher,
    private val analyticsTracker: AnalyticsTracker,
    dispatcherProvider: DispatcherProvider,
) : ViewModel() {

    private var lastTrackedTag: FragmentTag? = null

    init {
        viewModelScope.launch(dispatcherProvider.default()) {
            foregroundFragmentWatcher.foregroundFragment
                .filter {
                    it != lastTrackedTag
                }.onEach {
                    lastTrackedTag = it
                }.collect {
                    analyticsTracker.trackScreenView(it.tag)
                }
        }
    }
}
