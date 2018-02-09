package sk.styk.martin.apkanalyzer.ui.activity.appdetail.oninstall

import android.net.Uri
import sk.styk.martin.apkanalyzer.util.file.ApkFilePicker


/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
class OnInstallAppDetailPresenter : OnInstallAppDetailContract.Presenter {

    override lateinit var view: OnInstallAppDetailContract.View
    override var packagePath: String? = ""


    override fun initialize(uri: Uri?) {

        if (uri == null) {
            view.errorLoading()
        } else {
            packagePath = ApkFilePicker.getPathFromIntentData(uri) ?: return view.errorLoading()
        }
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