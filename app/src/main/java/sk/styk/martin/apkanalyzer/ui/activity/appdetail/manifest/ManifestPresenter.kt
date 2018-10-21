package sk.styk.martin.apkanalyzer.ui.activity.appdetail.manifest

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.view.View
import android.widget.Toast
import sk.styk.martin.apkanalyzer.ApkAnalyzer
import sk.styk.martin.apkanalyzer.ApkAnalyzer.Companion.context
import sk.styk.martin.apkanalyzer.business.analysis.task.AndroidManifestLoader
import sk.styk.martin.apkanalyzer.business.analysis.task.StringToFileSaveService
import sk.styk.martin.apkanalyzer.business.base.task.ApkAnalyzerAbstractAsyncLoader
import kotlin.properties.Delegates

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
class ManifestPresenter(
        private val loader: ApkAnalyzerAbstractAsyncLoader<String>,
        private val loaderManager: LoaderManager
) : LoaderManager.LoaderCallbacks<String>, ManifestContract.Presenter {

    override lateinit var view: ManifestContract.View
    private lateinit var packageName: String
    private var versionCode: Int by Delegates.notNull()
    private var versionName: String? = null

    private var manifest: String = ""

    override fun initialize(packageName: String, versionCode: Int, versionName: String?) {
        this.packageName = packageName
        this.versionCode = versionCode
        this.versionName = versionName
        startLoadingData()
        view.setUpViews()
    }

    private fun startLoadingData() {
        loaderManager.initLoader(AndroidManifestLoader.ID, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<String> {
        return loader
    }

    override fun onLoadFinished(loader: Loader<String>, data: String) {
        manifest = data
        view.hideLoading()
        view.showManifest(manifest)
    }

    override fun onLoaderReset(loader: Loader<String>) {
        manifest = ""
        view.showManifest(manifest)
    }

    /**
     * This is called only once permissions are granted
     */
    override fun saveManifest() {
        if (manifest.isBlank()) {
            view.makeSnackbar(ApkAnalyzer.context.getString(R.string.save_manifest_fail))
        } else {
            val targetFile = StringToFileSaveService.startService(context = context, packageName = packageName,
                    versionCode = versionCode, versionName = versionName, manifestContent = manifest)
            view.makeSnackbar(ApkAnalyzer.context.getString(R.string.save_manifest_background, targetFile), R.string.action_show, View.OnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_VIEW
                intent.setDataAndType(Uri.parse(targetFile), "text/xml")
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                try {
                    ApkAnalyzer.context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(ApkAnalyzer.context, R.string.activity_not_found_doc, Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}
