package sk.styk.martin.apkanalyzer.ui.activity.appdetail.actions

import android.Manifest
import android.app.Dialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.dialog_apk_actions.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.business.analysis.task.FileCopyService
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.actions.AppActionsContract.Companion.PACKAGE_TO_PERFORM_ACTIONS
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.actions.AppActionsContract.Companion.REQUEST_STORAGE_PERMISSION
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.manifest.ManifestActivity
import sk.styk.martin.apkanalyzer.ui.activity.repackageddetection.RepackagedDetectionFragment
import sk.styk.martin.apkanalyzer.util.file.AppOperations


/**
 * @author Martin Styk
 * @version 05.01.2018.
 */
class AppActionsDialog : DialogFragment(), AppActionsContract.View {

    private lateinit var presenter: AppActionsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = AppActionsPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_apk_actions, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.view = this
        presenter.initialize(arguments)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(context)
                .setView(R.layout.dialog_apk_actions)
                .setTitle(R.string.pick_action)
                .setNegativeButton(R.string.dismiss) { _, _ -> dismiss() }
                .create()
    }

    override fun onResume() {
        super.onResume()
        presenter.resume()
    }

    override fun setUpViews() {
        // setup buttons
        dialog.btn_copy.setOnClickListener { presenter.copyClick() }

        dialog.btn_share_apk.setOnClickListener { presenter.shareClick() }

        dialog.btn_show_app_google_play.setOnClickListener { presenter.showGooglePlayClick() }

        dialog.btn_repackaged_detection.setOnClickListener { presenter.repackagedDetectionClick() }

        dialog.btn_show_manifest.setOnClickListener { presenter.showManifestClick() }

        dialog.btn_show_app_system_page.setOnClickListener { presenter.showSystemPageClick() }

        dialog.btn_install_app.setOnClickListener { presenter.installAppClick() }
    }

    override fun showOnlyApkFileRelatedActions() {
        dialog.btn_show_manifest.visibility = View.GONE
        dialog.btn_show_app_system_page.visibility = View.GONE
        dialog.btn_install_app.visibility = View.VISIBLE
    }

    override fun createSnackbar(text: String) {
        Snackbar.make(activity.findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG).show()
    }

    override fun openRepackagedDetection(fragment: RepackagedDetectionFragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.container_frame, fragment)
                .addToBackStack(RepackagedDetectionFragment.TAG)
                .commit();
        logSelectEvent("repackaged-detection")
    }

    override fun openManifestActivity(packageName: String) {
        startActivity(ManifestActivity.createIntent(context, packageName))
        logSelectEvent("show-manifest")
    }

    override fun askForStoragePermission() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION)
        } else {
            presenter.exportApkFile()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.exportApkFile()
            } else {
                createSnackbar(getString(R.string.permission_not_granted))
            }
        }
    }

    override fun startApkExport(appDetailData: AppDetailData) {
        val targetFile = FileCopyService.startService(context, appDetailData)
        createSnackbar(context.getString(R.string.copy_apk_background, targetFile))
        logSelectEvent("export-apk")
    }

    override fun startSharingActivity(apkPath: String) {
        AppOperations.shareApkFile(context, apkPath)
        logSelectEvent("share-apk")
    }

    override fun openGooglePlay(packageName: String) {
        AppOperations.openGooglePlay(context, packageName)
        logSelectEvent("open-google-play")
    }

    override fun openSystemAboutActivity(packageName: String) {
        AppOperations.openAppSystemPage(context, packageName)
        logSelectEvent("open-system-about")
    }

    override fun startApkInstall(apkPath: String) {
        AppOperations.installPackage(context, apkPath)
        logSelectEvent("install-apk")
    }

    companion object {
        fun newInstance(appDetailData: AppDetailData): AppActionsDialog {
            val frag = AppActionsDialog()
            val args = Bundle()
            args.putParcelable(PACKAGE_TO_PERFORM_ACTIONS, appDetailData)
            frag.arguments = args
            return frag
        }
    }

    private fun logSelectEvent(itemId : String){
        val bundle =  Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemId);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "apk-action");
        FirebaseAnalytics.getInstance(context).logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

}