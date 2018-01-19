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

import sk.styk.martin.apkanalyzer.adapter.PermissionListAdapter
import sk.styk.martin.apkanalyzer.business.task.LocalPermissionsLoader
import sk.styk.martin.apkanalyzer.databinding.FragmentLocalPermissionsBinding
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData

/**
 * @author Martin Styk
 * @version 15.01.2017
 */
class LocalPermissionsFragment : Fragment(), LoaderManager.LoaderCallbacks<List<LocalPermissionData>>, LocalPermissionsLoader.ProgressCallback {

    private var binding: FragmentLocalPermissionsBinding? = null
    private var data: List<LocalPermissionData>? = null

    private var permissionListAdapter: PermissionListAdapter? = null


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        retainInstance = true
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentLocalPermissionsBinding.inflate(inflater)

        binding!!.recyclerViewPermissions.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))

        // We need to re-set callback of loader in case of configuration change
        val loader = loaderManager.initLoader(LocalPermissionsLoader.ID, null, this) as LocalPermissionsLoader
        loader?.setCallbackReference(this)

        return binding!!.root
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // Hide action bar item for searching
        menu!!.clear()

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onCreateLoader(id: Int, args: Bundle): Loader<List<LocalPermissionData>> {
        return LocalPermissionsLoader(context, this)
    }

    override fun onLoadFinished(loader: Loader<List<LocalPermissionData>>, data: List<LocalPermissionData>) {
        this.data = data

        permissionListAdapter = PermissionListAdapter(data)
        binding!!.recyclerViewPermissions.swapAdapter(permissionListAdapter, false)

        binding!!.content.visibility = View.VISIBLE
        binding!!.loadingBar.visibility = View.GONE
    }

    override fun onLoaderReset(loader: Loader<List<LocalPermissionData>>) {
        this.data = null
    }

    override fun onProgressChanged(currentProgress: Int, maxProgress: Int) {
        binding!!.loadingBar.setProgress(currentProgress, maxProgress)
    }

}
