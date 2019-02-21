package sk.styk.martin.apkanalyzer.ui.activity.appdetail.actions

import android.Manifest
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.annotation.StringRes
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.dialog_apk_actions.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import sk.styk.martin.apkanalyzer.ApkAnalyzer
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.business.analysis.task.DrawableSaveService
import sk.styk.martin.apkanalyzer.business.analysis.task.FileCopyService
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.actions.AppActionsContract.Companion.PACKAGE_TO_PERFORM_ACTIONS
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.manifest.ManifestActivity
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerFragment
import sk.styk.martin.apkanalyzer.util.file.AppOperations
import sk.styk.martin.apkanalyzer.util.file.toBitmap


/**
 * @author Martin Styk
 * @version 05.01.2018.
 */
@RuntimePermissions
class AppActionsDialog : DialogFragment(), AppActionsContract.View {

    private lateinit var presenter: AppActionsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = AppActionsPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_apk_actions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.view = this
        presenter.initialize(arguments ?: Bundle())
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(requireContext())
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
        dialog.btn_copy.setOnClickListener { presenter.exportClick() }

        dialog.btn_share_apk.setOnClickListener { presenter.shareClick() }

        dialog.btn_save_icon.setOnClickListener { presenter.saveIconClick() }

        dialog.btn_show_app_google_play.setOnClickListener { presenter.showGooglePlayClick() }

        dialog.btn_show_manifest.setOnClickListener { presenter.showManifestClick() }

        dialog.btn_show_app_system_page.setOnClickListener { presenter.showSystemPageClick() }

        dialog.btn_install_app.setOnClickListener { presenter.installAppClick() }
    }

    override fun showOnlyApkFileRelatedActions() {
        dialog.btn_show_manifest.visibility = View.GONE
        dialog.btn_show_app_system_page.visibility = View.GONE
        dialog.btn_install_app.visibility = View.VISIBLE
    }

    override fun createSnackbar(text: String, @StringRes actionName: Int?, action: View.OnClickListener?) {
        val parentPagerFragment = requireActivity().supportFragmentManager.findFragmentByTag(AppDetailPagerFragment.TAG)
        if (parentPagerFragment != null && parentPagerFragment is AppDetailPagerContract.View) {
            parentPagerFragment.createSnackbar(text, actionName, action)
        }
    }

    override fun openManifestActivity(appDetailData: AppDetailData) {
        startActivity(ManifestActivity.createIntent(requireContext(), appDetailData))
        logSelectEvent("show-manifest")
    }

    override fun startApkExport(appDetailData: AppDetailData) {
        exportApkWithPermissionCheck(appDetailData)
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun exportApk(appDetailData: AppDetailData) {
        val targetFile = FileCopyService.startService(requireContext(), appDetailData)
        createSnackbar(requireContext().getString(R.string.copy_apk_background, targetFile))
        logSelectEvent("export-apk")
        dismissAllowingStateLoss()
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onStorageDenied() {
        createSnackbar(getString(R.string.permission_not_granted))
        dismissAllowingStateLoss()
    }

    override fun startIconSave(appDetailData: AppDetailData) {
        saveIconWithPermissionCheck(appDetailData)
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun saveIcon(appDetailData: AppDetailData) {
        val targetFile = DrawableSaveService.startService(requireContext(), appDetailData,
                appDetailData.generalData.icon?.toBitmap())

        createSnackbar(requireContext().getString(R.string.save_icon_background, targetFile), R.string.action_show,
                View.OnClickListener {
                    val intent = Intent()
                    intent.action = Intent.ACTION_VIEW
                    intent.setDataAndType(Uri.parse(targetFile), "image/png")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    try {
                        ApkAnalyzer.context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(ApkAnalyzer.context, R.string.activity_not_found_image, Toast.LENGTH_LONG).show()
                    }
                })
        logSelectEvent("save-icon")
        dismissAllowingStateLoss()
    }

    override fun startSharingActivity(apkPath: String) {
        AppOperations.shareApkFile(requireContext(), apkPath)
        logSelectEvent("share-apk")
    }

    override fun openGooglePlay(packageName: String) {
        AppOperations.openGooglePlay(requireContext(), packageName)
        logSelectEvent("open-google-play")
    }

    override fun openSystemAboutActivity(packageName: String) {
        AppOperations.openAppSystemPage(requireContext(), packageName)
        logSelectEvent("open-system-about")
    }

    override fun startApkInstall(apkPath: String) {
        AppOperations.installPackage(requireContext(), apkPath)
        logSelectEvent("install-apk")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
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

    private fun logSelectEvent(itemId: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, itemId)
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "apk-action")
        FirebaseAnalytics.getInstance(requireContext()).logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

}