package sk.styk.martin.apkanalyzer.ui.premium

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.core.common.resources.ResourcesManager
import sk.styk.martin.apkanalyzer.manager.navigationdrawer.NavigationDrawerModel
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import javax.inject.Inject

@HiltViewModel
class PremiumFragmentViewModel @Inject constructor(
    private val navigationDrawerModel: NavigationDrawerModel,
    private val resourcesManager: ResourcesManager,
) : ViewModel() {

    private val openGooglePlayEvent = SingleLiveEvent<String>()
    val openGooglePlay: LiveData<String> = openGooglePlayEvent

    fun onUpgradeToPremiumClick() {
        openGooglePlayEvent.value = resourcesManager.getString(R.string.app_premium_package_name).toString()
    }

    fun onNavigationClick() = viewModelScope.launch {
        navigationDrawerModel.openDrawer()
    }
}
