package sk.styk.martin.apkanalyzer.ui.activity.appdetail.oninstall

import android.content.Context
import android.net.Uri
import sk.styk.martin.apkanalyzer.ui.base.BasePresenter

interface OnInstallAppDetailContract {
    interface View {
        fun setupViews()

        fun setupDetailFragment()

        fun errorLoading()

        fun requestStoragePermission()

        fun onPermissionRefused()
    }

    interface Presenter : BasePresenter<View> {
        var packageUri: Uri?

        fun initialize(uri: Uri?, context: Context)

        fun storagePermissionGranted()

        fun storagePermissionRefused()


    }
}