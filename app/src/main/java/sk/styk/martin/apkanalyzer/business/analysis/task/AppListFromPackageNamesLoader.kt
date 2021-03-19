package sk.styk.martin.apkanalyzer.business.analysis.task

import android.content.Context
import sk.styk.martin.apkanalyzer.business.analysis.logic.launcher.InstalledAppsManager
import sk.styk.martin.apkanalyzer.business.base.task.ApkAnalyzerAbstractAsyncLoader
import sk.styk.martin.apkanalyzer.model.list.AppListData

class AppListFromPackageNamesLoader(context: Context, private val packageNames: List<String>)
    : ApkAnalyzerAbstractAsyncLoader<List<AppListData>>(context) {

    private val appBasicDataService = InstalledAppsManager(getContext().packageManager)

    override fun loadInBackground(): List<AppListData> {
        return appBasicDataService.getForPackageNames(packageNames)
    }

    companion object {
        const val ID = 5
    }

}

