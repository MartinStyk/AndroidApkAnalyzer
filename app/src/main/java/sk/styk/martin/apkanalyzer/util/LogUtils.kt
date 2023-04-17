package sk.styk.martin.apkanalyzer.util

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import sk.styk.martin.apkanalyzer.BuildConfig
import timber.log.Timber

const val TAG_APP_DETAIL = "AppDetail"
const val TAG_EXPORTS = "Exports"
const val TAG_APP_ANALYSIS = "AppAnalysis"
const val TAG_APP_ACTIONS = "AppActions"

object LogUtils {

    fun logTrees() = if (BuildConfig.DEBUG) arrayOf(Timber.DebugTree(), FirebaseTree()) else arrayOf(FirebaseTree())

    class FirebaseTree : Timber.DebugTree() {

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            FirebaseCrashlytics.getInstance().log(crashlyticsMessage(priority, tag, message))
            if (priority >= Log.WARN && t != null) {
                FirebaseCrashlytics.getInstance().recordException(t)
            }
        }

        // format like in logcat "W/tag: warning message"
        private fun crashlyticsMessage(priority: Int, tag: String?, message: String): String {
            val priorityString = when (priority) {
                Log.ASSERT, Log.ERROR -> "E"
                Log.WARN -> "W"
                Log.INFO -> "I"
                Log.DEBUG -> "D"
                else -> "V"
            }
            return "$priorityString/$tag: $message"
        }
    }
}
