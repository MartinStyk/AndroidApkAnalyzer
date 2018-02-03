package sk.styk.martin.apkanalyzer.ui.activity.appdetail.actions

import android.os.Bundle
import sk.styk.martin.apkanalyzer.ApkAnalyzer.Companion.context
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.business.analysis.task.FileCopyService
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.activity.repackageddetection.RepackagedDetectionFragment
import sk.styk.martin.apkanalyzer.util.file.AppOperations


/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
class AppActionsPresenter(
) : AppActionsContract.Presenter {

    override lateinit var view: AppActionsContract.View
    private lateinit var appDetailData: AppDetailData

    override fun initialize(bundle: Bundle) {
        appDetailData = bundle.getParcelable(AppActionsContract.PACKAGE_TO_PERFORM_ACTIONS) ?: throw IllegalArgumentException()
    }

    override fun resume() {
        view.setUpViews()
        if(appDetailData.isAnalyzedApkFile)
            view.showOnlyApkFileRelatedActions()
    }

    override fun copyClick() {
        view.askForStoragePermission()
    }

    override fun shareClick() {
        AppOperations.shareApkFile(context, appDetailData.generalData.apkDirectory)
        view.dismiss()
    }

    override fun showGooglePlayClick() {
        AppOperations.openGooglePlay(context, appDetailData.generalData.packageName)
        view.dismiss()
    }

    override fun repackagedDetectionClick() {
        view.openRepackagedDetection(RepackagedDetectionFragment.newInstance(appDetailData))
        view.dismiss()
    }

    override fun showManifestClick() {
        view.openManifestActivity(appDetailData.generalData.packageName)
        view.dismiss()
    }

    override fun showSystemPageClick() {
        AppOperations.openAppSystemPage(context, appDetailData.generalData.packageName)
        view.dismiss()
    }

    override fun installAppClick() {
        AppOperations.installPackage(context, appDetailData.generalData.apkDirectory)
        view.dismiss()
    }

    override fun exportApkFile() {
        val targetFile = FileCopyService.startService(context, appDetailData)
        view.createSnackbar(context.getString(R.string.copy_apk_background, targetFile))
        view.dismiss()
    }

}