package sk.styk.martin.apkanalyzer.util

import android.content.Context
import android.content.Intent
import sk.styk.martin.apkanalyzer.activity.intro.IntroActivity

/**
 * @author Martin Styk
 * @version 15.11.2017.
 */
object FirstStartHelper {

    private const val FIRST_APP_START = "first_app_start"

    /**
     * Execute all actions related to app's first start
     */
    fun execute(context: Context) {
        if (isFirstStart(context))
            context.startActivity(Intent(context, IntroActivity::class.java))
    }

    fun isFirstStart(context: Context): Boolean {
        return SharedPreferencesHelper(context).readBoolean(FIRST_APP_START, true)
    }

    fun setFirstStartFinished(context: Context) {
        SharedPreferencesHelper(context).putBoolean(FIRST_APP_START, false)
    }
}
