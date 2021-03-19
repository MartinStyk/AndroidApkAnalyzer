package sk.styk.martin.apkanalyzer.ui.activity.permission.detail.pager

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import sk.styk.martin.apkanalyzer.ApkAnalyzer.Companion.context
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.activity.applist.AppListFragment
import sk.styk.martin.apkanalyzer.ui.activity.permission.detail.details.PermissionsGeneralDetailsFragment

private const val GENERAL_DETAILS_PAGE = 0
private const val GRANTED_APPS_PAGE = 1
private const val NOT_GRANTED_APPS_PAGE = 2

class PermissionDetailPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int) = when (position) {
        GENERAL_DETAILS_PAGE -> PermissionsGeneralDetailsFragment()
        GRANTED_APPS_PAGE -> AppListFragment()
        NOT_GRANTED_APPS_PAGE -> AppListFragment()
        else -> throw IllegalStateException()
    }

    override fun getCount(): Int = 0

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        GENERAL_DETAILS_PAGE -> context.resources.getString(R.string.permissions_detail)
        GRANTED_APPS_PAGE -> context.resources.getString(R.string.permissions_granted)
        NOT_GRANTED_APPS_PAGE -> context.resources.getString(R.string.permissions_not_granted)
        else -> throw IllegalStateException()
    }
}