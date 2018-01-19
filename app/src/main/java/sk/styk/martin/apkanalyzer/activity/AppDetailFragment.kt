package sk.styk.martin.apkanalyzer.activity

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.ContextCompat
import android.support.v4.content.Loader
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView

import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.activity.detailfragment.ManifestActivity
import sk.styk.martin.apkanalyzer.adapter.pager.AppDetailPagerAdapter
import sk.styk.martin.apkanalyzer.business.task.AppDetailLoader
import sk.styk.martin.apkanalyzer.business.task.FileCopyService
import sk.styk.martin.apkanalyzer.business.task.upload.AppDataUploadTask
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.util.file.AppOperations

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [AppListActivity]
 * in two-pane mode (on tablets) or a [AppDetailActivity]
 * on handsets.
 *
 * @author Martin Styk
 */
class AppDetailFragment : Fragment(), LoaderManager.LoaderCallbacks<AppDetailData>, View.OnClickListener {

    private var data: AppDetailData? = null
    private var packagePath: String? = null

    private var appBarLayout: CollapsingToolbarLayout? = null
    private var appBarLayuotImageView: ImageView? = null

    private var adapter: AppDetailPagerAdapter? = null
    private var loadingBar: ProgressBar? = null
    private var viewPager: ViewPager? = null
    private var errorLoadingText: TextView? = null

    private var isDialogShowing: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = AppDetailPagerAdapter(activity, fragmentManager)
        packagePath = arguments.getString(ARG_PACKAGE_PATH)
        loaderManager.initLoader(AppDetailLoader.ID, arguments, this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(sk.styk.martin.apkanalyzer.R.layout.fragment_app_detail, container, false)

        loadingBar = rootView.findViewById(R.id.item_detail_loading)
        appBarLayout = activity.findViewById(R.id.toolbar_layout)
        appBarLayuotImageView = activity.findViewById(R.id.toolbar_layout_image)
        errorLoadingText = rootView.findViewById(R.id.item_detail_error)

        viewPager = rootView.findViewById(R.id.pager)
        viewPager!!.adapter = adapter

        val tabLayout = rootView.findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)

        // if we are in 2-pane mode initialize floating button
        val actionButton = rootView.findViewById<FloatingActionButton>(R.id.btn_actions)
        actionButton?.setOnClickListener(this)

        if (data != null) {
            onLoadFinished(null, data)
        }

        return rootView
    }

    override fun onStart() {
        if (isDialogShowing) {
            showActionDialog()
        }
        super.onStart()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        outState!!.putBoolean("isDialogShowing", isDialogShowing)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        if (savedInstanceState != null)
            isDialogShowing = savedInstanceState.getBoolean("isDialogShowing")
    }

    override fun onCreateLoader(id: Int, args: Bundle): Loader<AppDetailData> {
        return AppDetailLoader(activity, args.getString(ARG_PACKAGE_NAME)!!, args.getString(ARG_PACKAGE_PATH)!!)
    }

    override fun onLoadFinished(loader: Loader<AppDetailData>?, data: AppDetailData?) {
        this.data = data
        loadingBar!!.visibility = View.GONE

        if (data == null) {
            errorLoadingText!!.visibility = View.VISIBLE
            if (appBarLayout != null) {
                appBarLayout!!.title = getString(R.string.loading_failed)
            }
        } else {
            if (appBarLayout != null) {
                appBarLayout!!.title = data.generalData.packageName
                appBarLayuotImageView!!.setImageDrawable(data.generalData.icon)
            }
            viewPager!!.visibility = View.VISIBLE

            adapter!!.dataChange(data)

            AppDataUploadTask(context).execute(data)
        }
    }

    override fun onLoaderReset(loader: Loader<AppDetailData>) {
        this.data = null
    }

    override fun onClick(view: View) {
        // show actions only when data is loaded
        if (data != null)
            showActionDialog()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_STORAGE_PERMISSION) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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
                .setNegativeButton(R.string.dismiss) { dialogInterface, i -> dialogInterface.dismiss() }
                .setOnDismissListener { isDialogShowing = false }.setOnCancelListener { isDialogShowing = false }.create()


        // setup buttons
        dialogView.findViewById<View>(R.id.btn_copy).setOnClickListener {
            dialog.dismiss()
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_STORAGE_PERMISSION)
            } else {
                exportApkFile()
            }
        }

        dialogView.findViewById<View>(R.id.btn_share_apk).setOnClickListener {
            AppOperations().shareApkFile(context, data!!.generalData.apkDirectory)
            dialog.dismiss()
        }

        dialogView.findViewById<View>(R.id.btn_show_app_google_play).setOnClickListener {
            dialog.dismiss()
            AppOperations().openGooglePlay(context, data!!.generalData.packageName)
        }

        // allow manifest and built-in app info only for installed packages
        if (data!!.isAnalyzedInstalledPackage) {
            dialogView.findViewById<View>(R.id.btn_show_manifest).setOnClickListener {
                //start manifest activity
                val intent = Intent(activity, ManifestActivity::class.java)
                intent.putExtra(ManifestActivity.PACKAGE_NAME_FOR_MANIFEST_REQUEST, data!!.generalData.packageName)

                dialog.dismiss()
                startActivity(intent)
            }

            dialogView.findViewById<View>(R.id.btn_show_app_system_page).setOnClickListener {
                dialog.dismiss()
                AppOperations().openAppSystemPage(context, data!!.generalData.packageName)
            }
        } else if (data!!.isAnalyzedApkFile) {
            dialogView.findViewById<View>(R.id.btn_show_manifest).visibility = View.GONE
            dialogView.findViewById<View>(R.id.btn_show_app_system_page).visibility = View.GONE
            dialogView.findViewById<View>(R.id.btn_install_app).visibility = View.VISIBLE

            dialogView.findViewById<View>(R.id.btn_install_app).setOnClickListener {
                dialog.dismiss()
                AppOperations().installPackage(context, packagePath!!)
            }
        }

        isDialogShowing = true
        dialog.show()
    }

    private fun exportApkFile() {
        val targetFile = FileCopyService.startService(activity, data!!)
        Snackbar.make(activity.findViewById(android.R.id.content), getString(R.string.copy_apk_background, targetFile), Snackbar.LENGTH_LONG).show()
    }

    companion object {

        val TAG = AppDetailFragment::class.java.simpleName
        val ARG_PACKAGE_NAME = "packageName"
        val ARG_PACKAGE_PATH = "packagePath"
        val ARG_CHILD = "dataForChild"
        private val REQUEST_STORAGE_PERMISSION = 11
    }
}
