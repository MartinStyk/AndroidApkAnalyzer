package sk.styk.martin.apkanalyzer.business.analysis.task

import android.content.Context
import sk.styk.martin.apkanalyzer.business.analysis.logic.launcher.AppBasicDataService
import sk.styk.martin.apkanalyzer.business.analysis.logic.launcher.LocalPermissionsDataService
import sk.styk.martin.apkanalyzer.business.base.task.ApkAnalyzerAbstractAsyncLoader
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionDataBuilder
import java.lang.ref.WeakReference

/**
 * @author Martin Styk
 * @version 15.01.2018
 */
class LocalPermissionsLoader(context: Context) : ApkAnalyzerAbstractAsyncLoader<List<LocalPermissionData>>(context) {

    private lateinit var callbackReference: WeakReference<ProgressCallback>
    private val appBasicDataService = AppBasicDataService(getContext().packageManager)
    private val localPermissionsDataService = LocalPermissionsDataService(getContext().packageManager)

    interface ProgressCallback {
        fun onProgressChanged(currentProgress: Int, maxProgress: Int)
    }

    fun setCallbackReference(progressCallback: ProgressCallback) {
        callbackReference = WeakReference(progressCallback)
    }

    override fun loadInBackground(): List<LocalPermissionData> {
        val allApps = appBasicDataService.getAll()
        val builder = LocalPermissionDataBuilder()

        for (i in allApps.indices) {
            val packageName = allApps[i].packageName

            builder.addAll(packageName, localPermissionsDataService.get(packageName))

            callbackReference.get()?.onProgressChanged(i, allApps.size)
        }

        return builder.build()
    }

    companion object {
        const val ID = 6
    }

}

