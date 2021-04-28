package sk.styk.martin.apkanalyzer.manager.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import sk.styk.martin.apkanalyzer.manager.navigationdrawer.ForegroundFragmentWatcher
import javax.inject.Inject

@ActivityScoped
class FragmentScreenTracker @Inject constructor(private val foregroundFragmentWatcher: ForegroundFragmentWatcher,
                                                private val analyticsTracker: AnalyticsTracker) : ViewModel() {

    init {
        viewModelScope.launch {
            foregroundFragmentWatcher.foregroundFragment.collect {
                analyticsTracker.trackScreenView(it.tag)
            }
        }
    }

}