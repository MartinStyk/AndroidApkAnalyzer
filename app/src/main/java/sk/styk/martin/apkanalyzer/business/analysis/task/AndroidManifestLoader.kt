package sk.styk.martin.apkanalyzer.business.analysis.task

import android.content.Context
import android.content.pm.PackageManager
import sk.styk.martin.apkanalyzer.business.analysis.logic.AndroidManifestService
import sk.styk.martin.apkanalyzer.business.base.task.ApkAnalyzerAbstractAsyncLoader

/**
 * Loader async task for loading android manifest content
 *
 * @author Martin Styk
 * @version 15.09.2017.
 */
class AndroidManifestLoader(context: Context, private val packageName: String, private val packagePath: String)
    : ApkAnalyzerAbstractAsyncLoader<String>(context) {

    private val packageManager: PackageManager = getContext().packageManager

    override fun loadInBackground(): String {
        return AndroidManifestService(packageManager, packageName, packagePath).loadAndroidManifest()
    }

    companion object {
        const val ID = 4
    }
}
