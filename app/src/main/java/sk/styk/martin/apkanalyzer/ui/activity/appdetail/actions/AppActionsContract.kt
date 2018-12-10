package sk.styk.martin.apkanalyzer.ui.activity.appdetail.actions

import android.os.Bundle
import androidx.annotation.StringRes
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.base.BasePresenter

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface AppActionsContract {
    interface View {
        fun createSnackbar(text: String, @StringRes actionName: Int? = null, action: android.view.View.OnClickListener? = null)

        fun openManifestActivity(appDetailData: AppDetailData)

        fun startApkExport(appDetailData: AppDetailData)

        fun startSharingActivity(apkPath : String)

        fun openGooglePlay(packageName: String)

        fun openSystemAboutActivity(packageName: String)

        fun startApkInstall(apkPath: String)

        fun startIconSave(appDetailData: AppDetailData)

        fun showMoreActionsDialog(appDetailData: AppDetailData)
    }

    interface Presenter : BasePresenter<View> {
        var appDetailData: AppDetailData

        fun initialize(bundle: Bundle)

        fun exportClick()

        fun shareClick()

        fun showGooglePlayClick()

        fun showManifestClick()

        fun showSystemPageClick()

        fun installAppClick()

        fun saveIconClick()

        fun showMoreClick()
    }

    companion object {
        const val PACKAGE_TO_PERFORM_ACTIONS = "package_to_perform_actions"
    }
}