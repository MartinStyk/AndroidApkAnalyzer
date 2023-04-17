package sk.styk.martin.apkanalyzer.ui.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch
import sk.styk.martin.apkanalyzer.manager.navigationdrawer.NavigationDrawerModel
import sk.styk.martin.apkanalyzer.manager.promo.UserReviewManager
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import timber.log.Timber

class AboutFragmentViewModel @AssistedInject constructor(
    private val navigationDrawerModel: NavigationDrawerModel,
    private val userReviewManager: UserReviewManager,
) : ViewModel() {

    private val openGooglePlayEvent = SingleLiveEvent<Unit>()
    val openGooglePlay: LiveData<Unit> = openGooglePlayEvent

    init {
        viewModelScope.launch {
            try {
                userReviewManager.openGooglePlayReview()
            } catch (e: Exception) {
                Timber.e(e, "Can not start GPlay review")
            }
        }
    }

    fun onRateAppClick() {
        openGooglePlayEvent.call()
    }

    fun onNavigationClick() = viewModelScope.launch {
        navigationDrawerModel.openDrawer()
    }

    @AssistedFactory // assisted to allow activity scoped injection
    interface Factory {
        fun create(): AboutFragmentViewModel
    }
}
