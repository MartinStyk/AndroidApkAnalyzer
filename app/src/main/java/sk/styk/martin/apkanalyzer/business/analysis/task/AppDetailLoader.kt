package sk.styk.martin.apkanalyzer.business.analysis.task

import android.content.Context
import android.net.Uri
import sk.styk.martin.apkanalyzer.business.analysis.logic.launcher.AppDetailDataService
import sk.styk.martin.apkanalyzer.business.base.task.ApkAnalyzerAbstractAsyncLoader
import sk.styk.martin.apkanalyzer.util.AppDetailDataExchange
import sk.styk.martin.apkanalyzer.util.file.FileUtils

class AppDetailLoader(context: Context,
                      private val packageName: String?,
                      private val packageUri: Uri?)
    : ApkAnalyzerAbstractAsyncLoader<String?>(context) {

    private val appDetailDataService = AppDetailDataService(getContext().packageManager)

    override fun loadInBackground(): String? {
        val appDetailData = appDetailDataService.get(packageName, FileUtils.uriToPatch(packageUri, context))
        return appDetailData?.let {
            AppDetailDataExchange.save(it)
        }
    }

    companion object {
        const val ID = 2
    }

}

