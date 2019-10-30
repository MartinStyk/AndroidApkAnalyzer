package sk.styk.martin.apkanalyzer.ui.activity.appdetail.actions

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_apk_actions.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.actions.AppActionsContract.Companion.PACKAGE_TO_PERFORM_ACTIONS
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerFragment.Companion.TAG

class AppActionsDialog : DialogFragment() {

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

        presenter.view = fragmentManager?.findFragmentByTag(TAG) as AppActionsContract.View
        presenter.initialize(arguments ?: Bundle())
    }

    override fun onResume() {
        super.onResume()
        setUpViews()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(requireContext())
                .setView(R.layout.dialog_apk_actions)
                .setTitle(R.string.pick_action)
                .setNegativeButton(R.string.dismiss) { _, _ -> dismiss() }
                .create()
    }

    private fun setUpViews() = dialog?.apply {
        findViewById<Button>(R.id.btn_copy)?.setOnClickListener {
            presenter.exportClick()
            dismissAllowingStateLoss()
        }
        btn_share_apk?.setOnClickListener {
            presenter.shareClick()
            dismissAllowingStateLoss()
        }
        btn_save_icon?.setOnClickListener {
            presenter.saveIconClick()
            dismissAllowingStateLoss()
        }
        btn_show_app_google_play.setOnClickListener {
            presenter.showGooglePlayClick()
            dismissAllowingStateLoss()
        }
        btn_show_manifest.setOnClickListener {
            presenter.showManifestClick()
            dismissAllowingStateLoss()
        }
        btn_show_app_system_page.setOnClickListener {
            presenter.showSystemPageClick()
            dismissAllowingStateLoss()
        }
        btn_install_app.setOnClickListener {
            presenter.installAppClick()
            dismissAllowingStateLoss()
        }

        if (presenter.appDetailData.isAnalyzedApkFile) {
            btn_show_manifest.visibility = View.GONE
            btn_show_app_system_page.visibility = View.GONE
            btn_install_app.visibility = View.VISIBLE
        }
    }

    companion object {
        fun newInstance(appDetailData: AppDetailData): AppActionsDialog {
            val frag = AppActionsDialog()
            val args = Bundle()
            args.putString(PACKAGE_TO_PERFORM_ACTIONS, appDetailData.generalData.packageName)
            frag.arguments = args
            return frag
        }
    }
}