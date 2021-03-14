package sk.styk.martin.apkanalyzer.ui.activity

import android.view.MenuItem
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.navigation.NavigationView
import sk.styk.martin.apkanalyzer.BuildConfig
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.manager.promo.StartPromoManager
import sk.styk.martin.apkanalyzer.util.AppFlavour
import sk.styk.martin.apkanalyzer.util.live.SingleLiveEvent
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(
        promoManager: StartPromoManager,
) :
        ViewModel(),
        NavigationView.OnNavigationItemSelectedListener {

    private val closeDrawerEvent = SingleLiveEvent<Unit>()
    val closeDrawer: LiveData<Unit> = closeDrawerEvent

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

    private val openFeatureDialogEvent = SingleLiveEvent<Unit>()
    val openFeatureDialog: LiveData<Unit> = openFeatureDialogEvent

    private val openOnboardingEvent = SingleLiveEvent<Unit>()
    val openOnboarding: LiveData<Unit> = openOnboardingEvent

    val premiumMenuItemVisible: LiveData<Boolean> = MutableLiveData(AppFlavour.isPremium && BuildConfig.SHOW_PROMO)

    init {
        placeInitialFragmentEvent.call()

        when (promoManager.getPromoAction()) {
            StartPromoManager.PromoResult.ONBOARDING -> openOnboardingEvent.call()
            StartPromoManager.PromoResult.FEATURE_DIALOG -> openFeatureDialogEvent.call()
            StartPromoManager.PromoResult.PROMO_DIALOG -> openPromoDialogEvent.call()
            StartPromoManager.PromoResult.NO_ACTION -> {
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

}