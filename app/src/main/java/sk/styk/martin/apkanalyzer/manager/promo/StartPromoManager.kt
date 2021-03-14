package sk.styk.martin.apkanalyzer.manager.promo

import android.content.Intent
import sk.styk.martin.apkanalyzer.BuildConfig
import sk.styk.martin.apkanalyzer.manager.persistence.PersistenceManager
import sk.styk.martin.apkanalyzer.ui.activity.dialog.FeatureDialog
import sk.styk.martin.apkanalyzer.ui.activity.dialog.PromoDialog
import sk.styk.martin.apkanalyzer.ui.activity.intro.IntroActivity
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StartPromoManager @Inject constructor(
        private val persistenceManager: PersistenceManager
) {

    /**
     * Execute all actions related to app's first start
     */
    fun getPromoAction(): PromoResult {
        return when {
            persistenceManager.isFirstStart -> PromoResult.ONBOARDING
            shouldShowPromo() -> PromoResult.PROMO_DIALOG
            shouldShowNewFeature() -> PromoResult.FEATURE_DIALOG
            else -> PromoResult.NO_ACTION
        }
    }

    private fun shouldShowPromo(): Boolean {
        if (!BuildConfig.SHOW_PROMO)
            return false

        val showTime = persistenceManager.lastPromoShowTime
        val currentTime = System.currentTimeMillis()
        val silentPeriod = TimeUnit.DAYS.toMillis(25)

        return if (showTime < 0 || showTime + silentPeriod < currentTime) {
            persistenceManager.lastPromoShowTime = currentTime
            true
        } else {
            false
        }
    }


    private fun shouldShowNewFeature(): Boolean {
        return if (FeatureDialog.fromVersion > persistenceManager.newFeatureShowVersion) {
            persistenceManager.newFeatureShowVersion = BuildConfig.VERSION_CODE.toLong()
            true
        } else {
            false
        }
    }

    enum class PromoResult {
        NO_ACTION,
        ONBOARDING,
        PROMO_DIALOG,
        FEATURE_DIALOG
    }

}