package sk.styk.martin.apkanalyzer.ui.activity.permission.list

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_local_permissions.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.business.analysis.task.LocalPermissionsLoader
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData
import sk.styk.martin.apkanalyzer.ui.activity.permission.detail.PermissionDetailActivity
import sk.styk.martin.apkanalyzer.ui.activity.permission.detail.pager.PermissionDetailPagerFragment

/**
 * @author Martin Styk
 * @version 15.01.2017
 */
class LocalPermissionsFragment : Fragment(), LocalPermissionsContract.View {

    private lateinit var presenter: LocalPermissionsContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        presenter = LocalPermissionsPresenter(LocalPermissionsLoader(context), loaderManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_local_permissions, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.view = this
        presenter.initialize()
    }

    override fun setUpViews() {
        recycler_view_permissions.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        // Hide action bar item for searching
        menu?.clear()
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun loadingStart() {
        content.visibility = View.GONE
        loading_bar.visibility = View.VISIBLE
    }

    override fun loadingFinished() {
        content.visibility = View.VISIBLE
        loading_bar.visibility = View.GONE
    }

    override fun showPermissionList() {
        recycler_view_permissions.swapAdapter(PermissionListAdapter(presenter), false)
    }

    override fun changeProgress(currentProgress: Int, maxProgress: Int) {
        loading_bar?.setProgress(currentProgress, maxProgress)
    }

    override fun openPermissionDetail(permission : LocalPermissionData) {
        val intent = Intent(context, PermissionDetailActivity::class.java)
        intent.putExtra(PermissionDetailPagerFragment.ARG_PERMISSIONS_DATA, permission)
        context.startActivity(intent)
    }

}
