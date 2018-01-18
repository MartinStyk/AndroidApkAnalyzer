package sk.styk.martin.apkanalyzer.business.task

import android.content.Context
import sk.styk.martin.apkanalyzer.business.service.launcher.AppBasicDataService
import sk.styk.martin.apkanalyzer.business.service.launcher.LocalApplicationStatisticDataService
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsData
import sk.styk.martin.apkanalyzer.model.statistics.LocalStatisticsDataBuilder
import java.lang.ref.WeakReference

/**
 * @author Martin Styk
 * @version 15.09.2018
 */
class LocalStatisticsLoader(context: Context, callback: ProgressCallback) : ApkAnalyzerAbstractAsyncLoader<LocalStatisticsData>(context) {

    private var callbackReference = WeakReference(callback)
    private val appBasicDataService = AppBasicDataService(context.packageManager)
    private val localAppDataService = LocalApplicationStatisticDataService(context.packageManager)

    interface ProgressCallback {
        fun onProgressChanged(currentProgress: Int, maxProgress: Int)
    }

    fun setCallbackReference(progressCallback: ProgressCallback) {
        callbackReference = WeakReference(progressCallback)
    }

    override fun loadInBackground(): LocalStatisticsData {
        val packageNames = appBasicDataService.getAllPackageNames()
        val builder = LocalStatisticsDataBuilder(packageNames.size)

        for (i in packageNames.indices) {
            builder.add(localAppDataService.get(packageNames[i]))

            callbackReference.get()?.onProgressChanged(i, packageNames.size)
        }

        return builder.build()
    }

    companion object {
        const val ID = 3
    }

}

