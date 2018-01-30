package sk.styk.martin.apkanalyzer.ui.activity.permission.detail.pager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_permission_detail_pager.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData
import sk.styk.martin.apkanalyzer.ui.activity.dialog.SimpleTextDialog

/**
 * @author Martin Styk
 * @version 15.01.2017
 */
class PermissionDetailPagerFragment : Fragment(), PermissionDetailPagerContract.View {

    private lateinit var adapter: PermissionDetailPagerAdapter
    private lateinit var presenter: PermissionDetailPagerContract.Presenter

    // we add description button to toolbar in this fragment
    private lateinit var description: MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        presenter = PermissionDetailPagerPresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_permission_detail_pager, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.view = this
        presenter.initialize(arguments)

    }

    override fun setUpViews() {
        adapter = PermissionDetailPagerAdapter(presenter, fragmentManager)
        pager.adapter = adapter
        tabs.setupWithViewPager(pager)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        description = menu!!.add(R.string.description)
        description.setIcon(R.drawable.ic_info_white)
        description.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (description.itemId.equals(item?.itemId))
            SimpleTextDialog.newInstance(getString(R.string.description), presenter.loadPermissionDescription(context.packageManager)).show(fragmentManager, SimpleTextDialog::class.java.simpleName)

        return super.onOptionsItemSelected(item)
    }

    companion object {

        val TAG = PermissionDetailPagerFragment::class.java.simpleName!!

        const val ARG_PERMISSIONS_DATA = "permission_args"
        const val ARG_CHILD = "permission_args_to_my_sweetest_child"

        fun create(permissionData: LocalPermissionData): PermissionDetailPagerFragment {
            val arguments = Bundle()
            arguments.putParcelable(ARG_PERMISSIONS_DATA, permissionData)
            val detailFragment = PermissionDetailPagerFragment()
            detailFragment.arguments = arguments
            return detailFragment
        }
    }

}
