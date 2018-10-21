package sk.styk.martin.apkanalyzer.business.analysis.task

import android.content.Context
import sk.styk.martin.apkanalyzer.business.analysis.logic.launcher.AppBasicDataService
import sk.styk.martin.apkanalyzer.business.base.task.ApkAnalyzerAbstractAsyncLoader
import sk.styk.martin.apkanalyzer.model.list.AppListData

/**
 * Loader async task for item for AppListDialog
 *
 * @author Martin Styk
 * @version 05.01.2018.
 */
class AppListFromPackageNamesLoader(context: Context, private val packageNames: List<String>)
    : ApkAnalyzerAbstractAsyncLoader<List<AppListData>>(context) {

    private val appBasicDataService = AppBasicDataService(getContext().packageManager)

    override fun loadInBackground(): List<AppListData> {
        return appBasicDataService.getForPackageNames(packageNames)
    }

    companion object {
        const val ID = 5
    }
}
