package sk.styk.martin.apkanalyzer.ui.activity.permission.detail.pager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import sk.styk.martin.apkanalyzer.ApkAnalyzer.Companion.context
import sk.styk.martin.apkanalyzer.R

/**
 * @author Martin Styk
 * @version 18.06.2017.
 */
class PermissionDetailPagerAdapter(private val presenter: PermissionDetailPagerPresenter, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? =
            when (position) {
                0 -> presenter.getGeneralDetailsFragment()
                1 -> presenter.getGrantedAppsFragment()
                2 -> presenter.getNotGrantedAppsFragment()
                else -> null
            }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> context.resources.getString(R.string.permissions_detail)
        1 -> context.resources.getString(R.string.permissions_granted)
        2 -> context.resources.getString(R.string.permissions_not_granted)
        else -> throw IllegalStateException()
    }
}