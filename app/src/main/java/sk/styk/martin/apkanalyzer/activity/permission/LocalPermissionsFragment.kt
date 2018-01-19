package sk.styk.martin.apkanalyzer.activity.permission

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.Loader
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_local_permissions.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.adapter.PermissionListAdapter
import sk.styk.martin.apkanalyzer.business.task.LocalPermissionsLoader
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData

/**
 * @author Martin Styk
 * @version 15.01.2017
 */
class LocalPermissionsFragment : Fragment(), LoaderManager.LoaderCallbacks<List<LocalPermissionData>>, LocalPermissionsLoader.ProgressCallback {

    private var data: List<LocalPermissionData>? = null

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_local_permissions, container, false)

        // We need to re-set callback of loader in case of configuration change
        val loader = loaderManager.initLoader(LocalPermissionsLoader.ID, null, this) as LocalPermissionsLoader
        loader.setCallbackReference(this)

        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view_permissions.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // Hide action bar item for searching
        menu?.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<List<LocalPermissionData>> {
        return LocalPermissionsLoader(context, this)
    }

    override fun onLoadFinished(loader: Loader<List<LocalPermissionData>>, data: List<LocalPermissionData>) {
        this.data = data

        recycler_view_permissions.swapAdapter(PermissionListAdapter(data), false)

        content.visibility = View.VISIBLE
        loading_bar.visibility = View.GONE
    }

    override fun onLoaderReset(loader: Loader<List<LocalPermissionData>>) {
        data = null
    }

    override fun onProgressChanged(currentProgress: Int, maxProgress: Int) {
        loading_bar?.setProgress(currentProgress, maxProgress)
    }

}
