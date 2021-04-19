package sk.styk.martin.apkanalyzer.ui.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sk.styk.martin.apkanalyzer.manager.navigationdrawer.NavigationDrawerModel
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class AboutFragmentViewModel @Inject constructor(
        private val navigationDrawerModel: NavigationDrawerModel
) : ViewModel() {

    private val openGooglePlayEvent = SingleLiveEvent<Unit>()
    val openGooglePlay: LiveData<Unit> = openGooglePlayEvent

    fun onRateAppClick() = openGooglePlayEvent.call()

    fun onNavigationClick() = viewModelScope.launch {
        navigationDrawerModel.openDrawer()
    }

}