package sk.styk.martin.apkanalyzer.activity.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DividerItemDecoration

import java.util.ArrayList

import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.adapter.AppListRecyclerAdapter
import sk.styk.martin.apkanalyzer.business.task.AppListFromPackageNamesLoader
import sk.styk.martin.apkanalyzer.databinding.DialogAppListBinding
import sk.styk.martin.apkanalyzer.model.list.AppListData

/**
 * @author Martin Styk
 * @version 05.01.2018.
 */
class AppListDialog : DialogFragment(), LoaderManager.LoaderCallbacks<List<AppListData>> {
    private var binding: DialogAppListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loaderManager.initLoader(AppListFromPackageNamesLoader.ID, arguments, this)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAppListBinding.inflate(activity.layoutInflater)

        binding!!.recyclerViewApplications.adapter = AppListRecyclerAdapter(ArrayList(0))
        val dividerItemDecoration = DividerItemDecoration(binding!!.recyclerViewApplications.context, DividerItemDecoration.VERTICAL)
        binding!!.recyclerViewApplications.addItemDecoration(dividerItemDecoration)


        return AlertDialog.Builder(activity)
                .setView(binding!!.root)
                .setTitle(R.string.apps)
                .setNegativeButton(R.string.dismiss) { dialogInterface, i -> dismiss() }
                .create()
    }

    override fun onCreateLoader(id: Int, args: Bundle): Loader<List<AppListData>> {
        return AppListFromPackageNamesLoader(context, args.getStringArrayList(PACKAGES)!!)
    }

    override fun onLoadFinished(loader: Loader<List<AppListData>>, data: List<AppListData>) {
        binding!!.recyclerViewApplications.adapter = AppListRecyclerAdapter(data)
    }

    override fun onLoaderReset(loader: Loader<List<AppListData>>) {}

    companion object {

        val PACKAGES = "packages"

        fun newInstance(packageNames: ArrayList<String>): AppListDialog {
            val frag = AppListDialog()
            val args = Bundle()
            args.putStringArrayList(PACKAGES, packageNames)
            frag.arguments = args
            return frag
        }
    }
}