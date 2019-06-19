package sk.styk.martin.apkanalyzer.business.analysis.livedata

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.AsyncTask
import androidx.lifecycle.MutableLiveData
import sk.styk.martin.apkanalyzer.ApkAnalyzer.Companion.context
import sk.styk.martin.apkanalyzer.business.analysis.logic.launcher.AppBasicDataService
import sk.styk.martin.apkanalyzer.model.list.AppListData

/**
 * @author Martin Styk {@literal <martin.styk@gmail.com>}
 */
class AppListLiveData(context: Context) : MutableLiveData<List<AppListData>?>() {
    private val appListDataService = AppBasicDataService(context.packageManager)
    private var packageIntentReceiver =PackageIntentReceiver(context)

    init {
        load()
    }

    fun load() = AsyncTask.execute {
        postValue(appListDataService.getAll())
    }

    fun dispose() = packageIntentReceiver.unregister()

    inner class PackageIntentReceiver(context: Context) : BroadcastReceiver() {

        init {
            val filter = IntentFilter(Intent.ACTION_PACKAGE_ADDED)
            filter.addAction(Intent.ACTION_PACKAGE_REMOVED)
            filter.addAction(Intent.ACTION_PACKAGE_CHANGED)
            filter.addDataScheme("package")
            context.registerReceiver(this, filter)

            // Register for events related to sdcard installation.
            val sdFilter = IntentFilter()
            sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE)
            sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE)
            context.registerReceiver(this, sdFilter)
        }

        override fun onReceive(context: Context, intent: Intent) {
            load()
        }

        fun unregister() {
            context.unregisterReceiver(this)
        }
    }

}