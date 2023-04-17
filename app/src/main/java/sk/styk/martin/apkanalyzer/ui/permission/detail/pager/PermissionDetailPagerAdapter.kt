package sk.styk.martin.apkanalyzer.ui.permission.detail.pager

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.permission.detail.apps.PermissionsAppListFragment
import sk.styk.martin.apkanalyzer.ui.permission.detail.details.PermissionsGeneralDetailsFragment

private const val GENERAL_DETAILS_PAGE = 0
private const val GRANTED_APPS_PAGE = 1
private const val NOT_GRANTED_APPS_PAGE = 2

class PermissionDetailPagerAdapter(
    val bundle: Bundle,
    val context: Context,
    fm: FragmentManager,
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int) = when (position) {
        GENERAL_DETAILS_PAGE -> PermissionsGeneralDetailsFragment()
        GRANTED_APPS_PAGE -> PermissionsAppListFragment.newInstance(granted = true)
        NOT_GRANTED_APPS_PAGE -> PermissionsAppListFragment.newInstance(granted = false)
        else -> throw IllegalStateException()
    }.apply {
        val fragmentArgs = arguments
        arguments = bundle.apply {
            fragmentArgs?.let {
                putAll(fragmentArgs)
            }
        }
    }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        GENERAL_DETAILS_PAGE -> context.resources.getString(R.string.permissions_detail)
        GRANTED_APPS_PAGE -> context.resources.getString(R.string.permissions_granted)
        NOT_GRANTED_APPS_PAGE -> context.resources.getString(R.string.permissions_not_granted)
        else -> throw IllegalStateException()
    }
}
