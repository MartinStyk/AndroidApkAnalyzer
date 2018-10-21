package sk.styk.martin.apkanalyzer.business.analysis.task

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import sk.styk.martin.apkanalyzer.business.analysis.logic.launcher.AppBasicDataService
import sk.styk.martin.apkanalyzer.business.base.task.ApkAnalyzerAbstractAsyncLoader
import sk.styk.martin.apkanalyzer.model.list.AppListData

/**
 * Loader async task for items for list view on AppListFragment
 *
 * @author Martin Styk
 * @version 15.06.2017.
 */
class AppListLoader(context: Context) : ApkAnalyzerAbstractAsyncLoader<List<AppListData>>(context) {

    private val installedAppsService = AppBasicDataService(getContext().packageManager)

    private var mPackageObserver: PackageIntentReceiver? = null

    override fun loadInBackground(): List<AppListData> {
        return installedAppsService.getAll()
    }

    /**
     * Handles a request to start the Loader.
     */
    override fun onStartLoading() {
        // Start watching for changes in the app data.
        if (mPackageObserver == null) {
            mPackageObserver = PackageIntentReceiver(this)
        }

        super.onStartLoading()
    }

    override fun onReset() {
        // Stop monitoring for changes.
        if (mPackageObserver != null) {
            context.unregisterReceiver(mPackageObserver)
            mPackageObserver = null
        }

        super.onReset()
    }

    /**
     * Helper class to look for interesting changes to the installed apps
     * so that the loader can be updated.
     */
    class PackageIntentReceiver(private val mLoader: AppListLoader) : BroadcastReceiver() {

        init {
            val filter = IntentFilter(Intent.ACTION_PACKAGE_ADDED)
            filter.addAction(Intent.ACTION_PACKAGE_REMOVED)
            filter.addAction(Intent.ACTION_PACKAGE_CHANGED)
            filter.addDataScheme("package")
            mLoader.context.registerReceiver(this, filter)

            // Register for events related to sdcard installation.
            val sdFilter = IntentFilter()
            sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE)
            sdFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE)
            mLoader.context.registerReceiver(this, sdFilter)
        }

        override fun onReceive(context: Context, intent: Intent) {
            // Tell the loader about the change.
            mLoader.onContentChanged()
        }
    }

    companion object {
        const val ID = 1
    }
}
