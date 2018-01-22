package sk.styk.martin.apkanalyzer.activity.permission

import android.content.pm.PackageManager
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
import sk.styk.martin.apkanalyzer.activity.dialog.SimpleTextDialog
import sk.styk.martin.apkanalyzer.adapter.pager.PermissionsPagerAdapter
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData

/**
 * @author Martin Styk
 * @version 15.01.2017
 */
class PermissionDetailPagerFragment : Fragment() {

    private lateinit var adapter: PermissionsPagerAdapter

    // we add description button to toolbar in this fragment
    private lateinit var description: MenuItem
    private var permissionDescription: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

        adapter = PermissionsPagerAdapter(activity, fragmentManager)
        val permissionData = arguments.getParcelable<LocalPermissionData>(ARG_PERMISSIONS_DATA)
                ?: throw IllegalArgumentException("data null")

        loadPermissionDescription(context.packageManager, permissionData.permissionData.name)

        adapter.dataChange(permissionData)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_permission_detail_pager, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            SimpleTextDialog.newInstance(getString(R.string.description), permissionDescription).show(fragmentManager, SimpleTextDialog::class.java.simpleName)

        return super.onOptionsItemSelected(item)
    }

    private fun loadPermissionDescription(packageManager: PackageManager, permissionName: String) {
        Thread({
            var desc: CharSequence? = null
            try {
                desc = packageManager.getPermissionInfo(permissionName, PackageManager.GET_META_DATA).loadDescription(packageManager)
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            permissionDescription = if (desc == null) context.getString(R.string.NA) else desc.toString()
        }).start()
    }

    companion object {

        val TAG = PermissionDetailPagerFragment::class.java.simpleName!!

        const val ARG_PERMISSIONS_DATA = "permission_args"
        const val ARG_CHILD = "permission_args_to_my_sweetest_child"

        fun create(permissionData: LocalPermissionData): PermissionDetailPagerFragment {
            val arguments = Bundle()
            arguments.putParcelable(PermissionDetailPagerFragment.ARG_PERMISSIONS_DATA, permissionData)
            val detailFragment = PermissionDetailPagerFragment()
            detailFragment.arguments = arguments
            return detailFragment
        }
    }

}
