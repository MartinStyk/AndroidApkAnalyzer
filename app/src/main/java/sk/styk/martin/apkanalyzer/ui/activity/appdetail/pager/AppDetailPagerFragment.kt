package sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.activity_app_detail.app_bar
import kotlinx.android.synthetic.main.activity_app_detail.toolbar_layout
import kotlinx.android.synthetic.main.activity_app_detail.toolbar_layout_image
import kotlinx.android.synthetic.main.fragment_app_detail.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions
import sk.styk.martin.apkanalyzer.ApkAnalyzer
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.business.analysis.task.AppDetailLoader
import sk.styk.martin.apkanalyzer.business.analysis.task.DrawableSaveService
import sk.styk.martin.apkanalyzer.business.analysis.task.FileCopyService
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.actions.*
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.manifest.ManifestActivity
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract.Companion.ARG_PACKAGE_NAME
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract.Companion.ARG_PACKAGE_PATH
import sk.styk.martin.apkanalyzer.util.BackPressedListener
import sk.styk.martin.apkanalyzer.util.DisplayHelper
import sk.styk.martin.apkanalyzer.util.file.AppOperations
import sk.styk.martin.apkanalyzer.util.file.toBitmap
import sk.styk.martin.apkanalyzer.views.FloatingActionButton

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [MainActivity]
 * in two-pane mode (on tablets) or a [AppDetailActivity]
 * on handsets.
 */
@RuntimePermissions
class AppDetailPagerFragment : Fragment(), AppDetailPagerContract.View, AppActionsContract.View, BackPressedListener {

    private lateinit var adapter: AppDetailPagerAdapter
    lateinit var pagerPresenter: AppDetailPagerContract.Presenter
    lateinit var appActionsPresenter: AppActionsContract.Presenter

    private var actionButton: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pagerPresenter = AppDetailPagerPresenter(AppDetailLoader(context = requireContext(),
                packageName = arguments?.getString(ARG_PACKAGE_NAME),
                packageUri = arguments?.getParcelable(ARG_PACKAGE_PATH)), LoaderManager.getInstance(this))
        appActionsPresenter = AppActionsPresenter()
        adapter = AppDetailPagerAdapter(requireContext(), fragmentManager!!, pagerPresenter)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_app_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pagerPresenter.view = this
        pagerPresenter.initialize(arguments!!)

        appActionsPresenter.view = this
    }

    override fun onBackPressed(): Boolean {
        return if (actionButton?.isSpeedDialMenuOpen == true) {
            actionButton?.closeSpeedDialMenu()
            true
        } else {
            false
        }
    }

    override fun setUpViews() {
        pager.adapter = adapter
        tabs.setupWithViewPager(pager)
    }

    override fun hideLoading() {
        item_detail_loading.visibility = View.GONE
    }

    override fun showLoadingFailed() {
        item_detail_error.visibility = View.VISIBLE
        activity?.toolbar_layout?.title = getString(R.string.loading_failed)
    }

    override fun showAppDetails(packageName: String, icon: Drawable?) {
        appActionsPresenter.initialize(Bundle().apply { putString(AppActionsContract.PACKAGE_TO_PERFORM_ACTIONS, packageName) })

        activity?.toolbar_layout?.title = packageName
        activity?.toolbar_layout_image?.setImageDrawable(icon)
        actionButton = btn_actions ?: activity?.findViewById(R.id.btn_actions)
        actionButton?.let {
            val displayHeight = DisplayHelper.displayHeightDp(requireContext())
            it.speedDialMenuAdapter = when (appActionsPresenter.appDetailData.analysisMode) {
                AppDetailData.AnalysisMode.APK_FILE ->
                    if (displayHeight < ApkFileActionsSpeedMenu.REQUIRED_HEIGHT_DP) {
                        ApkFileActionsSpeedMenuMinimal(appActionsPresenter)
                    } else {
                        ApkFileActionsSpeedMenu(appActionsPresenter)
                    }
                AppDetailData.AnalysisMode.INSTALLED_PACKAGE ->
                    if (displayHeight < InstalledAppActionsSpeedMenu.REQUIRED_HEIGHT_DP) {
                        InstalledAppActionsSpeedMenuMinimal(appActionsPresenter)
                    } else {
                        InstalledAppActionsSpeedMenu(appActionsPresenter)
                    }
            }

            it.contentCoverEnabled = true
            it.visibility = View.VISIBLE
            it.show()
        }

        activity?.app_bar?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (Math.abs(verticalOffset) - appBarLayout.totalScrollRange == 0) {
                actionButton?.hide()
            } else {
                actionButton?.show()
            }
        })

        pager.visibility = View.VISIBLE
    }

    // APK Actions

    override fun createSnackbar(text: String, @StringRes actionName: Int?, action: View.OnClickListener?) {
        val snackbar = Snackbar.make(requireActivity().findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG)
        if (action != null && actionName != null)
            snackbar.setAction(actionName, action)
        snackbar.show()
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
    }

    @OnPermissionDenied(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun onStorageDenied() {
        createSnackbar(getString(R.string.permission_not_granted))
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
                    intent.setAction(Intent.ACTION_VIEW)
                    intent.setDataAndType(Uri.parse(targetFile), "image/png")
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    try {
                        ApkAnalyzer.context.startActivity(intent)
                    } catch (e: ActivityNotFoundException) {
                        Toast.makeText(ApkAnalyzer.context, R.string.activity_not_found_image, Toast.LENGTH_LONG).show()
                    }
                })
        logSelectEvent("save-icon")
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

    override fun showMoreActionsDialog(appDetailData: AppDetailData) {
        AppActionsDialog.newInstance(appDetailData).show(requireFragmentManager(), AppActionsDialog::class.java.simpleName)
        logSelectEvent("show-more")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    private fun logSelectEvent(itemId: String) {
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.ITEM_ID, itemId)
            putString(FirebaseAnalytics.Param.CONTENT_TYPE, "apk-action")
        }
        FirebaseAnalytics.getInstance(requireContext()).logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle)
    }

    companion object {

        val TAG = AppDetailPagerFragment::class.java.simpleName

        fun create(packageName: String? = null, packageUri: Uri? = null) =
                AppDetailPagerFragment().apply {
                    arguments = Bundle().apply {
                        putParcelable(ARG_PACKAGE_PATH, packageUri)
                        putString(ARG_PACKAGE_NAME, packageName)
                    }
                }
    }
}
