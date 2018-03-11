package sk.styk.martin.apkanalyzer.ui.activity.appdetail.manifest

import android.support.annotation.StringRes
import sk.styk.martin.apkanalyzer.ui.base.BasePresenter

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface ManifestContract {
    interface View {
        fun setUpViews()

        fun hideLoading()

        fun showManifest(manifest: String)

        fun makeSnackbar(text: String, @StringRes actionName: Int? = null, action: android.view.View.OnClickListener? = null)
    }

    interface Presenter : BasePresenter<View> {
        fun initialize(packageName: String)

        fun saveManifest()
    }

    companion object {
        const val PACKAGE_NAME_FOR_MANIFEST_REQUEST = "packageNameForManifestRequest"
    }
}