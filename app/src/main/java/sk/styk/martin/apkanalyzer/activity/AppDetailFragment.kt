package sk.styk.martin.apkanalyzer.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.ContextCompat
import android.support.v4.content.Loader
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_app_detail.toolbar_layout
import kotlinx.android.synthetic.main.activity_app_detail.toolbar_layout_image
import kotlinx.android.synthetic.main.dialog_apk_actions.view.*
import kotlinx.android.synthetic.main.fragment_app_detail.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.activity.detailfragment.ManifestActivity
import sk.styk.martin.apkanalyzer.activity.dialog.RepackagedDetectionDialog
import sk.styk.martin.apkanalyzer.adapter.pager.AppDetailPagerAdapter
import sk.styk.martin.apkanalyzer.business.analysis.task.AppDetailLoader
import sk.styk.martin.apkanalyzer.business.analysis.task.FileCopyService
import sk.styk.martin.apkanalyzer.business.upload.task.AppDataUploadTask
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.util.file.AppOperations

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [MainActivity]
 * in two-pane mode (on tablets) or a [AppDetailActivity]
 * on handsets.
 *
 * @author Martin Styk
 */
class AppDetailFragment : Fragment(), LoaderManager.LoaderCallbacks<AppDetailData?>, View.OnClickListener {

    private var data: AppDetailData? = null
    private var packagePath: String? = null

    private lateinit var adapter: AppDetailPagerAdapter

    private var isDialogShowing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = AppDetailPagerAdapter(context, fragmentManager)
        packagePath = arguments.getString(ARG_PACKAGE_PATH)
        loaderManager.initLoader(AppDetailLoader.ID, arguments, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_app_detail, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pager.adapter = adapter
        tabs.setupWithViewPager(pager)

        // if we are in 2-pane mode initialize floating button
        btn_actions?.setOnClickListener(this)

        if (data != null)
            onLoadFinished(null, data)
    }

    override fun onStart() {
        if (isDialogShowing)
            showActionDialog()

        super.onStart()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState?.putBoolean("isDialogShowing", isDialogShowing)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        isDialogShowing = savedInstanceState?.getBoolean("isDialogShowing") ?: false
    }

    override fun onCreateLoader(id: Int, args: Bundle): Loader<AppDetailData?> {
        return AppDetailLoader(context, args.getString(ARG_PACKAGE_NAME), args.getString(ARG_PACKAGE_PATH))
    }

    override fun onLoadFinished(loader: Loader<AppDetailData?>?, data: AppDetailData?) {
        this.data = data
        item_detail_loading.visibility = View.GONE

        if (data == null) {
            item_detail_error.visibility = View.VISIBLE
            activity?.toolbar_layout?.title = getString(R.string.loading_failed)
        } else {
            activity?.toolbar_layout?.title = data.generalData.packageName
            activity?.toolbar_layout_image?.setImageDrawable(data.generalData.icon)

            pager.visibility = View.VISIBLE

            adapter.dataChange(data)

            AppDataUploadTask().execute(data)
        }
    }

    override fun onLoaderReset(loader: Loader<AppDetailData?>) {
        data = null
    }

    override fun onClick(view: View) {
        // show actions only when data is loaded
        if (data != null)
            showActionDialog()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                exportApkFile()
            } else {
                Snackbar.make(activity.findViewById(android.R.id.content), R.string.permission_not_granted, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun showActionDialog() {
        // if data load failed or data is not loaded so far, do not show action dialogs
        if (data == null)
            return

        val factory = LayoutInflater.from(context)
        val dialogView = factory.inflate(R.layout.dialog_apk_actions, null)

        // setup dialog
        val dialog = AlertDialog.Builder(activity)
                .setTitle(getString(R.string.pick_action))
                .setView(dialogView)
                .setNegativeButton(R.string.dismiss) { dialogInterface, _ -> dialogInterface.dismiss() }
                .setOnDismissListener { isDialogShowing = false }
                .setOnCancelListener { isDialogShowing = false }
                .create()

        // setup buttons
        dialogView.btn_copy.setOnClickListener {
            dialog.dismiss()
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION)
            } else {
                exportApkFile()
            }
        }

        dialogView.btn_share_apk.setOnClickListener {
            AppOperations.shareApkFile(context, data?.generalData?.apkDirectory
                    ?: return@setOnClickListener)
            dialog.dismiss()
        }

        dialogView.btn_show_app_google_play.setOnClickListener {
            dialog.dismiss()
            AppOperations.openGooglePlay(context, data?.generalData?.packageName
                    ?: return@setOnClickListener)
        }

        dialogView.btn_repackaged_detection.setOnClickListener {
            RepackagedDetectionDialog.newInstance(data!!).show(fragmentManager, RepackagedDetectionDialog::class.java.simpleName)
        }

        // allow manifest and built-in app info only for installed packages
        if (data != null && data!!.isAnalyzedInstalledPackage) {
            dialogView.btn_show_manifest.setOnClickListener {
                //start manifest activity
                val intent = Intent(activity, ManifestActivity::class.java)
                intent.putExtra(ManifestActivity.PACKAGE_NAME_FOR_MANIFEST_REQUEST, data?.generalData?.packageName
                        ?: return@setOnClickListener)

                dialog.dismiss()
                startActivity(intent)
            }

            dialogView.btn_show_app_system_page.setOnClickListener {
                dialog.dismiss()
                AppOperations.openAppSystemPage(context, data?.generalData?.packageName
                        ?: return@setOnClickListener)
            }
        } else if (data!!.isAnalyzedApkFile) {
            dialogView.btn_show_manifest.visibility = View.GONE
            dialogView.btn_show_app_system_page.visibility = View.GONE
            dialogView.btn_install_app.visibility = View.VISIBLE

            dialogView.btn_install_app.setOnClickListener {
                dialog.dismiss()
                AppOperations.installPackage(context, packagePath ?: return@setOnClickListener)
            }
        }

        isDialogShowing = true
        dialog.show()
    }

    private fun exportApkFile() {
        val targetFile = FileCopyService.startService(activity, data ?: return)
        Snackbar.make(activity.findViewById(android.R.id.content), getString(R.string.copy_apk_background, targetFile), Snackbar.LENGTH_LONG).show()
    }

    companion object {

        val TAG = AppDetailFragment::class.java.simpleName!!
        const val ARG_PACKAGE_NAME = "packageName"
        const val ARG_PACKAGE_PATH = "packagePath"
        const val ARG_CHILD = "dataForChild"
        private const val REQUEST_STORAGE_PERMISSION = 11

        fun create(packageName: String? = null, packagePath: String? = null): AppDetailFragment {
            val arguments = Bundle()
            arguments.putString(AppDetailFragment.ARG_PACKAGE_PATH, packagePath)
            arguments.putString(AppDetailFragment.ARG_PACKAGE_NAME, packageName)
            val detailFragment = AppDetailFragment()
            detailFragment.arguments = arguments

            return detailFragment
        }
    }
}
