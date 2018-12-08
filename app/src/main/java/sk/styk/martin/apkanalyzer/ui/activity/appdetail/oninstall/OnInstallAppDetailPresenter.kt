package sk.styk.martin.apkanalyzer.ui.activity.appdetail.oninstall

import android.content.Context
import android.net.Uri


/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
class OnInstallAppDetailPresenter : OnInstallAppDetailContract.Presenter {

    override lateinit var view: OnInstallAppDetailContract.View
    override var packageUri: Uri? = null


    override fun initialize(uri: Uri?, context: Context) {
        packageUri = uri ?: return view.errorLoading()
        view.requestStoragePermission()
    }

    override fun storagePermissionGranted() {
        view.setupViews()
        view.setupDetailFragment()
    }

    override fun storagePermissionRefused() {
        view.setupViews()
        view.onPermissionRefused()
    }
}