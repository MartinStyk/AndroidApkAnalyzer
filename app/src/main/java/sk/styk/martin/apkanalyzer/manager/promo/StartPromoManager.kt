package sk.styk.martin.apkanalyzer.manager.promo

import sk.styk.martin.apkanalyzer.BuildConfig
import sk.styk.martin.apkanalyzer.manager.persistence.PersistenceManager
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class StartPromoManager @Inject constructor(
    private val persistenceManager: PersistenceManager,
) {

    fun getPromoAction(): PromoResult {
        return when {
            persistenceManager.isOnboardingRequired -> PromoResult.ONBOARDING
            shouldShowPromo() -> PromoResult.PROMO_DIALOG
            persistenceManager.appStartNumber >= 3 -> PromoResult.INAPP_RATE_DIALOG // Inner GPlay handling ensures dialog is not shown too often
            else -> PromoResult.NO_ACTION
        }
    }

    private fun shouldShowPromo(): Boolean {
        if (!BuildConfig.SHOW_PROMO) {
            return false
        }

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
        INAPP_RATE_DIALOG,
    }
}
