package sk.styk.martin.apkanalyzer.ui.activity.applist

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_app_list.*
import sk.styk.martin.apkanalyzer.business.analysis.task.AppListFromPackageNamesLoader
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.base.AppDetailActivity
import sk.styk.martin.apkanalyzer.ui.activity.applist.AppListContract.Companion.PACKAGES_ARGUMENT
import java.util.*


/**
 * @author Martin Styk
 * @version 05.01.2018.
 */
class AppListDialog : DialogFragment(), AppListContract.View {

    private lateinit var presenter: AppListContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = AppListPresenter(AppListFromPackageNamesLoader(requireContext(), arguments!!.getStringArrayList(PACKAGES_ARGUMENT)), loaderManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_app_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.view = this
        presenter.initialize(arguments!!)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(requireContext())
                .setView(R.layout.dialog_app_list)
                .setTitle(R.string.apps)
                .setNegativeButton(R.string.dismiss) { _, _ -> dismiss() }
                .create()
    }

    override fun setUpViews() {
        dialog.recycler_view_applications.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    override fun hideLoading() {
        dialog.list_view_progress_bar.visibility = View.GONE
    }

    override fun nothingToDisplay() {
        dialog.list_view_progress_bar.visibility = View.GONE
        dialog.nothing_to_show.visibility = View.VISIBLE
    }

    override fun showAppList() {
        dialog.recycler_view_applications.adapter = AppListRecyclerAdapter(presenter)
    }

    override fun openAppDetailActivity(packageName: String) {
        requireContext().startActivity(AppDetailActivity.createIntent(packageName, null, requireContext()))
    }

    companion object {
        fun newInstance(packageNames: ArrayList<String>): AppListDialog {
            val frag = AppListDialog()
            val args = Bundle()
            args.putStringArrayList(PACKAGES_ARGUMENT, packageNames)
            frag.arguments = args
            return frag
        }
    }
}