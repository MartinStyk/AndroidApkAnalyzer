package sk.styk.martin.apkanalyzer.adapter.pager

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.activity.permission.PermissionDetailPagerFragment
import sk.styk.martin.apkanalyzer.activity.permission.PermissionsAppListFragment
import sk.styk.martin.apkanalyzer.activity.permission.PermissionsGeneralDetailsFragment
import sk.styk.martin.apkanalyzer.model.detail.PermissionData
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData
import java.util.*

/**
 * @author Martin Styk
 * @version 18.06.2017.
 */
class PermissionsPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    private var grantedPackages: List<String> = ArrayList(0)
    private var notGrantedPackages: List<String> = ArrayList(0)
    private var permissionData: PermissionData? = null

    override fun getItem(position: Int): Fragment? {
        val fragment: Fragment
        val args = Bundle()
        when (position) {
            0 -> {
                args.putParcelable(PermissionDetailPagerFragment.ARG_CHILD, permissionData)
                args.putInt(PermissionsGeneralDetailsFragment.ARG_NUMBER_GRANTED_APPS, grantedPackages.size)
                args.putInt(PermissionsGeneralDetailsFragment.ARG_NUMBER_NOT_GRANTED_APPS, notGrantedPackages.size)
                fragment = PermissionsGeneralDetailsFragment()
            }

            1 -> {
                args.putStringArrayList(PermissionDetailPagerFragment.ARG_CHILD, grantedPackages as ArrayList<String>)
                fragment = PermissionsAppListFragment()
            }

            2 -> {
                args.putStringArrayList(PermissionDetailPagerFragment.ARG_CHILD, notGrantedPackages as ArrayList<String>)
                fragment = PermissionsAppListFragment()
            }

            else -> return null
        }
        fragment.arguments = args
        return fragment
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> context.resources.getString(R.string.permissions_detail)
        1 -> context.resources.getString(R.string.permissions_granted)
        2 -> context.resources.getString(R.string.permissions_not_granted)
        else -> throw IllegalStateException()
    }
    
    fun dataChange(data: LocalPermissionData) {
        this.permissionData = data.permissionData
        this.grantedPackages = data.grantedPackageNames
        this.notGrantedPackages = data.notGrantedPackageNames
    }
}