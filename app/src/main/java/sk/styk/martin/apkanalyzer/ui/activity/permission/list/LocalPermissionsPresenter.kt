package sk.styk.martin.apkanalyzer.ui.activity.permission.list

import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import sk.styk.martin.apkanalyzer.business.analysis.task.LocalPermissionsLoader
import sk.styk.martin.apkanalyzer.business.base.task.ApkAnalyzerAbstractAsyncLoader
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData


/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
class LocalPermissionsPresenter(
        private val loader: ApkAnalyzerAbstractAsyncLoader<List<LocalPermissionData>>,
        private val loaderManager: LoaderManager
) : LocalPermissionsContract.Presenter,
        LoaderManager.LoaderCallbacks<List<LocalPermissionData>>, LocalPermissionsLoader.ProgressCallback {

    override lateinit var view: LocalPermissionsContract.View
    private var data: List<LocalPermissionData>? = null

    override fun initialize() {
        view.setUpViews()
        startLoadingData()
    }

    override fun onProgressChanged(currentProgress: Int, maxProgress: Int) {
        view.changeProgress(currentProgress, maxProgress)
    }

    // Data loading part
    private fun startLoadingData() {
        view.loadingStart()
        loaderManager.initLoader(LocalPermissionsLoader.ID, null, this)
        (loader as LocalPermissionsLoader).setCallbackReference(this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<LocalPermissionData>> {
        return loader
    }

    override fun onLoadFinished(loader: Loader<List<LocalPermissionData>>?, result: List<LocalPermissionData>) {
        data = result
        view.loadingFinished()
        view.showPermissionList()
    }

    override fun permissionCount(): Int {
        return data?.size ?: 0
    }

    override fun permissionSelected(position: Int) {
        return data?.let { view.openPermissionDetail(it[position]) } ?: throw IllegalStateException()
    }

    override fun onBindPermissionViewOnPosition(position: Int, holder: LocalPermissionsContract.ItemView) {
        return data?.let {
            val item = it[position]
            holder.permissionName = item.permissionData.name
            holder.permissionSimpleName = item.permissionData.simpleName
            holder.affectedApps = item.permissionStatusList.size
        } ?: throw IllegalStateException()
    }

    override fun onLoaderReset(loader: Loader<List<LocalPermissionData>>?) {}
}