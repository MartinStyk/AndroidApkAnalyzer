package sk.styk.martin.apkanalyzer.activity.permission


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import sk.styk.martin.apkanalyzer.adapter.AppListRecyclerAdapter
import sk.styk.martin.apkanalyzer.business.task.AppListFromPackageNamesLoader
import sk.styk.martin.apkanalyzer.databinding.FragmentPermissionAppListBinding
import sk.styk.martin.apkanalyzer.model.list.AppListData

/**
 * @author Martin Styk
 * @version 15.01.2017
 */
class PermissionsAppListFragment : Fragment(), LoaderManager.LoaderCallbacks<List<AppListData>> {

    private var binding: FragmentPermissionAppListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loaderManager.initLoader(AppListFromPackageNamesLoader.ID, arguments, this)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPermissionAppListBinding.inflate(layoutInflater)

        binding!!.recyclerViewAppList.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        return binding!!.root
    }

    override fun onCreateLoader(id: Int, args: Bundle): Loader<List<AppListData>> {
        return AppListFromPackageNamesLoader(context, args.getStringArrayList(PermissionDetailPagerFragment.ARG_CHILD)!!)
    }

    override fun onLoadFinished(loader: Loader<List<AppListData>>, data: List<AppListData>?) {
        binding!!.listViewProgressBar.visibility = View.GONE
        if (data != null && !data.isEmpty())
            binding!!.recyclerViewAppList.adapter = AppListRecyclerAdapter(data)
        else
            binding!!.nothingToShow.visibility = View.VISIBLE

    }

    override fun onLoaderReset(loader: android.support.v4.content.Loader<List<AppListData>>) {}
}
