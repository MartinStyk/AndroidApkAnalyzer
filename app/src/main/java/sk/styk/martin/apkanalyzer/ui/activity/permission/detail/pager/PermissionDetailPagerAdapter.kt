package sk.styk.martin.apkanalyzer.ui.activity.permission.detail.pager

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import sk.styk.martin.apkanalyzer.ApkAnalyzer.Companion.context
import sk.styk.martin.apkanalyzer.ui.activity.applist.AppListContract
import sk.styk.martin.apkanalyzer.ui.activity.applist.AppListFragment
import sk.styk.martin.apkanalyzer.ui.activity.permission.detail.details.PermissionsGeneralDetailsFragment

/**
 * @author Martin Styk
 * @version 18.06.2017.
 */
class PermissionDetailPagerAdapter(private val presenter: PermissionDetailPagerContract.Presenter, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        val fragment: Fragment
        val args = Bundle()
        when (position) {
            0 -> {
                args.putParcelable(PermissionDetailPagerFragment.ARG_CHILD, presenter.localPermissionData.permissionData)
                args.putInt(PermissionsGeneralDetailsFragment.ARG_NUMBER_GRANTED_APPS, presenter.localPermissionData.grantedPackageNames.size)
                args.putInt(PermissionsGeneralDetailsFragment.ARG_NUMBER_NOT_GRANTED_APPS, presenter.localPermissionData.notGrantedPackageNames.size)
                fragment = PermissionsGeneralDetailsFragment()
            }

            1 -> {
                args.putStringArrayList(AppListContract.PACKAGES_ARGUMENT, presenter.localPermissionData.grantedPackageNames as ArrayList<String>)
                fragment = AppListFragment()
            }

            2 -> {
                args.putStringArrayList(AppListContract.PACKAGES_ARGUMENT, presenter.localPermissionData.notGrantedPackageNames as ArrayList<String>)
                fragment = AppListFragment()
            }

            else -> throw IllegalStateException()
        }
        fragment.arguments = args
        return fragment
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> context.resources.getString(R.string.permissions_detail)
        1 -> context.resources.getString(R.string.permissions_granted)
        2 -> context.resources.getString(R.string.permissions_not_granted)
        else -> throw IllegalStateException()
    }
}