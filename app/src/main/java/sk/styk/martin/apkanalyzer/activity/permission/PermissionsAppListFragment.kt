package sk.styk.martin.apkanalyzer.activity.permission


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_permission_app_list.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.adapter.AppListRecyclerAdapter
import sk.styk.martin.apkanalyzer.business.task.AppListFromPackageNamesLoader
import sk.styk.martin.apkanalyzer.model.list.AppListData

/**
 * @author Martin Styk
 * @version 15.01.2017
 */
class PermissionsAppListFragment : Fragment(), LoaderManager.LoaderCallbacks<List<AppListData>> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        loaderManager.initLoader(AppListFromPackageNamesLoader.ID, arguments, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_permission_app_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view_app_list.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    override fun onCreateLoader(id: Int, args: Bundle): Loader<List<AppListData>> {
        return AppListFromPackageNamesLoader(context, args.getStringArrayList(PermissionDetailPagerFragment.ARG_CHILD)
                ?: emptyList())
    }

    override fun onLoadFinished(loader: Loader<List<AppListData>>, data: List<AppListData>) {
        list_view_progress_bar.visibility = View.GONE
        if (data.isEmpty())
            nothing_to_show.visibility = View.VISIBLE
        else
            recycler_view_app_list.adapter = AppListRecyclerAdapter(data)
    }

    override fun onLoaderReset(loader: android.support.v4.content.Loader<List<AppListData>>) {}
}
