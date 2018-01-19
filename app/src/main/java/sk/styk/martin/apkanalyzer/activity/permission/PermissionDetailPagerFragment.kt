package sk.styk.martin.apkanalyzer.activity.permission

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.activity.dialog.SimpleTextDialog
import sk.styk.martin.apkanalyzer.adapter.pager.PermissionsPagerAdapter
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData

/**
 * @author Martin Styk
 * @version 15.01.2017
 */
class PermissionDetailPagerFragment : Fragment() {

    private var adapter: PermissionsPagerAdapter? = null

    // we add description button to toolbar in this fragment
    private var description: MenuItem? = null
    private var permissionDescription: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        adapter = PermissionsPagerAdapter(activity, fragmentManager)
        val permissionData = arguments.getParcelable<LocalPermissionData>(ARG_PERMISSIONS_DATA)

        if (permissionData != null)
            loadPermissionDescription(context.packageManager, permissionData.permissionData.name)

        adapter!!.dataChange(permissionData)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater!!.inflate(R.layout.fragment_permission_detail_pager, container, false)

        val viewPager = rootView.findViewById<ViewPager>(R.id.pager)
        viewPager.adapter = adapter

        val tabLayout = rootView.findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(viewPager)

        return rootView
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        description = menu!!.add(R.string.description)
        description!!.setIcon(R.drawable.ic_info_white)
        description!!.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == description!!.itemId) {
            SimpleTextDialog.newInstance(getString(R.string.description), permissionDescription!!).show(fragmentManager, SimpleTextDialog::class.java.simpleName)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadPermissionDescription(packageManager: PackageManager, permissionName: String) {
        object : Thread() {
            override fun run() {
                var desc: CharSequence? = null
                try {
                    desc = packageManager.getPermissionInfo(permissionName, PackageManager.GET_META_DATA).loadDescription(packageManager)
                } catch (e: PackageManager.NameNotFoundException) {
                    e.printStackTrace()
                }

                permissionDescription = if (desc == null) context.getString(R.string.NA) else desc.toString()
            }
        }.start()
    }

    companion object {

        val TAG = PermissionDetailPagerFragment::class.java.simpleName

        val ARG_PERMISSIONS_DATA = "permission_args"
        val ARG_CHILD = "permission_args_to_my_sweetest_child"
    }

}
