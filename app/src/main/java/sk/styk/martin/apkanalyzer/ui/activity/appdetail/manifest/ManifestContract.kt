package sk.styk.martin.apkanalyzer.ui.activity.appdetail.manifest

import androidx.annotation.StringRes
import sk.styk.martin.apkanalyzer.ui.base.BasePresenter

interface ManifestContract {
    interface View {
        fun setUpViews()

        fun hideLoading()

        fun showManifest(manifest: String)

        fun makeSnackbar(text: String, @StringRes actionName: Int? = null, action: android.view.View.OnClickListener? = null)
    }

    interface Presenter : BasePresenter<View> {
        fun initialize(packageName: String, versionCode: Int, versionName: String?)

        fun saveManifest()
    }

    companion object {
        const val PACKAGE_NAME_FOR_MANIFEST_REQUEST = "packageNameForManifestRequest"
        const val PACKAGE_PATH_FOR_MANIFEST_REQUEST = "packagePathForManifestRequest"
        const val PACKAGE_VERSION_CODE_FOR_MANIFEST_REQUEST = "packageVersionCodeForManifestRequest"
        const val PACKAGE_VERSION_NAME_FOR_MANIFEST_REQUEST = "packageVersionNameForManifestRequest"
    }
}