package sk.styk.martin.apkanalyzer.manager.promo

import sk.styk.martin.apkanalyzer.BuildConfig
import sk.styk.martin.apkanalyzer.manager.persistence.PersistenceManager
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
            persistenceManager.isOnboardingRequired -> PromoResult.ONBOARDING
            shouldShowPromo() -> PromoResult.PROMO_DIALOG
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

    enum class PromoResult {
        NO_ACTION,
        ONBOARDING,
        PROMO_DIALOG,
    }

}