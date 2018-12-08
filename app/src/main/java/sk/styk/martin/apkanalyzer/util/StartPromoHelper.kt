package sk.styk.martin.apkanalyzer.util

import android.content.Context
import android.content.Intent
import sk.styk.martin.apkanalyzer.BuildConfig
import sk.styk.martin.apkanalyzer.ui.activity.dialog.PromoDialog
import sk.styk.martin.apkanalyzer.ui.activity.intro.IntroActivity
import java.util.concurrent.TimeUnit

/**
 * @author Martin Styk
 * @version 15.11.2017.
 */
object StartPromoHelper {

    private const val FIRST_APP_START = "first_app_start"
    private const val PROMO_SHOW_TIME = "promo_shown"

    /**
     * Execute all actions related to app's first start
     */
    fun execute(context: Context) {
        if (isFirstStart(context))
            context.startActivity(Intent(context, IntroActivity::class.java))
        else if (shouldShowPromo(context) && context is PromoDialog.PromoDialogController)
            context.onPromoDialogShowRequested()
    }

    private fun isFirstStart(context: Context): Boolean {
        return SharedPreferencesHelper(context).readBoolean(FIRST_APP_START, true)
    }

    private fun shouldShowPromo(context: Context): Boolean {
        if (BuildConfig.FLAVOR == AppFlavour.PREMIUM)
            return false

        val shownTime = SharedPreferencesHelper(context).readLong(PROMO_SHOW_TIME, -1)
        val currentTime = System.currentTimeMillis()
        val silentPeriod = TimeUnit.DAYS.toMillis(10)

        return if (shownTime < 0 || shownTime + silentPeriod < currentTime) {
            SharedPreferencesHelper(context).putLong(PROMO_SHOW_TIME, currentTime)
            true
        } else false
    }

    fun setFirstStartFinished(context: Context) {
        SharedPreferencesHelper(context).putBoolean(FIRST_APP_START, false)
    }
}
