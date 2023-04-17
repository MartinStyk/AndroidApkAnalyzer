package sk.styk.martin.apkanalyzer.ui.main

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.navigation.NavigationView
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import sk.styk.martin.apkanalyzer.BuildConfig
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.manager.navigationdrawer.ForegroundFragmentWatcher
import sk.styk.martin.apkanalyzer.manager.navigationdrawer.NavigationDrawerModel
import sk.styk.martin.apkanalyzer.manager.persistence.PersistenceManager
import sk.styk.martin.apkanalyzer.manager.promo.StartPromoManager
import sk.styk.martin.apkanalyzer.manager.promo.UserReviewManager
import sk.styk.martin.apkanalyzer.util.AppFlavour
import sk.styk.martin.apkanalyzer.util.FragmentTag
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import timber.log.Timber

class MainActivityViewModel @AssistedInject constructor(
    promoManager: StartPromoManager,
    persistenceManager: PersistenceManager,
    private val navigationDrawerModel: NavigationDrawerModel,
    private val userReviewManager: UserReviewManager,
    private val foregroundFragmentWatcher: ForegroundFragmentWatcher,
) : ViewModel(), NavigationView.OnNavigationItemSelectedListener {

    private val closeDrawerEvent = SingleLiveEvent<Unit>()
    val closeDrawer: LiveData<Unit> = closeDrawerEvent

    private val openDrawerEvent = SingleLiveEvent<Unit>()
    val openDrawer: LiveData<Unit> = openDrawerEvent

    private val placeInitialFragmentEvent = SingleLiveEvent<Unit>()
    val placeInitialFragment: LiveData<Unit> = placeInitialFragmentEvent

    private val openAppListEvent = SingleLiveEvent<Unit>()
    val openAppList: LiveData<Unit> = openAppListEvent

    private val openStatisticsEvent = SingleLiveEvent<Unit>()
    val openStatistics: LiveData<Unit> = openStatisticsEvent

    private val openPermissionsEvent = SingleLiveEvent<Unit>()
    val openPermissions: LiveData<Unit> = openPermissionsEvent

    private val openSettingsEvent = SingleLiveEvent<Unit>()
    val openSettings: LiveData<Unit> = openSettingsEvent

    private val openAboutEvent = SingleLiveEvent<Unit>()
    val openAbout: LiveData<Unit> = openAboutEvent

    private val openPremiumEvent = SingleLiveEvent<Unit>()
    val openPremium: LiveData<Unit> = openPremiumEvent

    private val openPromoDialogEvent = SingleLiveEvent<Unit>()
    val openPromoDialog: LiveData<Unit> = openPromoDialogEvent

    private val openOnboardingEvent = SingleLiveEvent<Unit>()
    val openOnboarding: LiveData<Unit> = openOnboardingEvent

    private val selectedMenuItemLiveData = MutableLiveData<Int>()
    val selectedMenuItem: LiveData<Int> = selectedMenuItemLiveData

    val premiumMenuItemVisible: LiveData<Boolean> = MutableLiveData(AppFlavour.isPremium && BuildConfig.SHOW_PROMO)

    init {
        persistenceManager.appStartNumber++

        placeInitialFragmentEvent.call()

        when (promoManager.getPromoAction()) {
            StartPromoManager.PromoResult.ONBOARDING -> openOnboardingEvent.call()
            StartPromoManager.PromoResult.PROMO_DIALOG -> openPromoDialogEvent.call()
            StartPromoManager.PromoResult.INAPP_RATE_DIALOG -> viewModelScope.launch(CoroutineExceptionHandler { _, throwable -> Timber.e(throwable) }) {
                userReviewManager.openGooglePlayReview()
            }
            StartPromoManager.PromoResult.NO_ACTION -> {
            }
        }

        viewModelScope.launch {
            navigationDrawerModel.handleState().collect {
                if (it) openDrawerEvent.call() else closeDrawerEvent.call()
            }
        }

        viewModelScope.launch {
            foregroundFragmentWatcher.foregroundFragment.collect {
                when (it) {
                    FragmentTag.AppList -> selectedMenuItemLiveData.value = R.id.nav_app_list
                    FragmentTag.LocalStatistics -> selectedMenuItemLiveData.value = R.id.nav_local_stats
                    FragmentTag.LocalPermissions -> selectedMenuItemLiveData.value = R.id.nav_local_permissions
                    FragmentTag.Settings -> selectedMenuItemLiveData.value = R.id.nav_settings
                    FragmentTag.About -> selectedMenuItemLiveData.value = R.id.nav_about
                    FragmentTag.Premium -> selectedMenuItemLiveData.value = R.id.nav_premium
                    else -> {}
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_app_list -> openAppListEvent.call()
            R.id.nav_local_stats -> openStatisticsEvent.call()
            R.id.nav_local_permissions -> openPermissionsEvent.call()
            R.id.nav_settings -> openSettingsEvent.call()
            R.id.nav_about -> openAboutEvent.call()
            R.id.nav_premium -> openPremiumEvent.call()
        }

        closeDrawerEvent.call()
        return true
    }

    @AssistedFactory
    interface Factory {
        fun create(): MainActivityViewModel
    }
}
