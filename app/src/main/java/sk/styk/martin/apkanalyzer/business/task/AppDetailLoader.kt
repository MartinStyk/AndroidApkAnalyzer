package sk.styk.martin.apkanalyzer.business.task

import android.content.Context

import sk.styk.martin.apkanalyzer.business.service.launcher.AppDetailDataService
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData

/**
 * Loader async task for item for AppDetailFragment and AppDetailActivity
 *
 * @author Martin Styk
 * @version 15.06.2017.
 */
class AppDetailLoader(context: Context, private val packageName: String?, private val pathToPackage: String?)
    : ApkAnalyzerAbstractAsyncLoader<AppDetailData?>(context) {

    private val appDetailDataService = AppDetailDataService(context.packageManager)

    override fun loadInBackground(): AppDetailData? {
        return appDetailDataService.get(packageName, pathToPackage)
    }

    companion object {
        const val ID = 2
    }

}

