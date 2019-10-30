package sk.styk.martin.apkanalyzer.ui.activity.permission.detail.pager

import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import sk.styk.martin.apkanalyzer.ApkAnalyzer.Companion.context
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.activity.applist.AppListContract
import sk.styk.martin.apkanalyzer.ui.activity.applist.AppListFragment
import sk.styk.martin.apkanalyzer.ui.activity.permission.detail.details.PermissionsGeneralDetailsFragment

private const val GENERAL_DETAILS_PAGE =  0
private const val GRANTED_APPS_PAGE =  1
private const val NOT_GRANTED_APPS_PAGE =  2

class PermissionDetailPagerAdapter(private val presenter: PermissionDetailPagerContract.Presenter, fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT ) {

    override fun getItem(position: Int) = when (position) {
        GENERAL_DETAILS_PAGE -> {
            PermissionsGeneralDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(PermissionDetailPagerFragment.ARG_CHILD, presenter.localPermissionData.permissionData)
                    putInt(PermissionsGeneralDetailsFragment.ARG_NUMBER_GRANTED_APPS, presenter.localPermissionData.grantedPackageNames.size)
                    putInt(PermissionsGeneralDetailsFragment.ARG_NUMBER_NOT_GRANTED_APPS, presenter.localPermissionData.notGrantedPackageNames.size)
                }
            }
        }
        GRANTED_APPS_PAGE -> {
            AppListFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(AppListContract.PACKAGES_ARGUMENT, presenter.localPermissionData.grantedPackageNames as ArrayList<String>)
                }
            }
        }
        NOT_GRANTED_APPS_PAGE -> {
            AppListFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(AppListContract.PACKAGES_ARGUMENT, presenter.localPermissionData.notGrantedPackageNames as ArrayList<String>)
                }
            }
        }

        else -> throw IllegalStateException()
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        GENERAL_DETAILS_PAGE -> context.resources.getString(R.string.permissions_detail)
        GRANTED_APPS_PAGE -> context.resources.getString(R.string.permissions_granted)
        NOT_GRANTED_APPS_PAGE -> context.resources.getString(R.string.permissions_not_granted)
        else -> throw IllegalStateException()
    }
}