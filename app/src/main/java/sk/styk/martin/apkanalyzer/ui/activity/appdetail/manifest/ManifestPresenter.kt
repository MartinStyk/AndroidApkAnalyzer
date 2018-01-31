package sk.styk.martin.apkanalyzer.ui.activity.appdetail.manifest

import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import sk.styk.martin.apkanalyzer.ApkAnalyzer.Companion.context
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.business.analysis.task.AndroidManifestLoader
import sk.styk.martin.apkanalyzer.business.analysis.task.StringToFileSaveService
import sk.styk.martin.apkanalyzer.business.base.task.ApkAnalyzerAbstractAsyncLoader


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
    private var manifest: String = ""

    override fun initialize(packageName: String) {
        this.packageName = packageName
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

    override fun saveManifestWithPermissionCheck() {
        return view.askForStoragePermission()
    }

    /**
     * This is called only once permissions are granted
     */
    override fun saveManifest() {
        if (manifest.isBlank()) {
            view.makeSnackbar(R.string.save_manifest_fail)
        } else {
            val targetFile = StringToFileSaveService.startService(context = context, packageName = packageName, manifestContent = manifest)
            view.makeSnackbar(R.string.save_manifest_background, targetFile)
        }

    }
}