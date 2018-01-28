package sk.styk.martin.apkanalyzer.ui.activity.dialog

import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_app_list.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.adapter.AppListRecyclerAdapter
import sk.styk.martin.apkanalyzer.business.analysis.task.AppListFromPackageNamesLoader
import sk.styk.martin.apkanalyzer.model.list.AppListData
import java.util.*

/**
 * @author Martin Styk
 * @version 05.01.2018.
 */
class AppListDialog : DialogFragment(), LoaderManager.LoaderCallbacks<List<AppListData>> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loaderManager.initLoader(AppListFromPackageNamesLoader.ID, arguments, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_app_list, container, false)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return AlertDialog.Builder(context)
                .setView(R.layout.dialog_app_list)
                .setTitle(R.string.apps)
                .setNegativeButton(R.string.dismiss) { _, _ -> dismiss() }
                .create()
    }

    override fun onCreateLoader(id: Int, args: Bundle): Loader<List<AppListData>> {
        return AppListFromPackageNamesLoader(context, args.getStringArrayList(PACKAGES)!!)
    }

    override fun onLoadFinished(loader: Loader<List<AppListData>>, data: List<AppListData>) {

        dialog.recycler_view_applications.adapter = AppListRecyclerAdapter(data)
        dialog.recycler_view_applications.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    override fun onLoaderReset(loader: Loader<List<AppListData>>) {}

    companion object {

        private const val PACKAGES = "packages"

        fun newInstance(packageNames: ArrayList<String>): AppListDialog {
            val frag = AppListDialog()
            val args = Bundle()
            args.putStringArrayList(PACKAGES, packageNames)
            frag.arguments = args
            return frag
        }
    }
}