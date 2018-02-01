package sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import sk.styk.martin.apkanalyzer.R

/**
 * @author Martin Styk
 * @version 18.06.2017.
 */
class AppDetailPagerAdapter(
        private val context: Context,
        fm: FragmentManager,
        private val presenter: AppDetailPagerContract.Presenter) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? =
            when (position) {
                0 -> presenter.getGeneralDetailsFragment()
                1 -> presenter.getCertificateDetailsFragment()
                2 -> presenter.getResourceDetailsFragment()
                3 -> presenter.getActivityDetailsFragment()
                4 -> presenter.getServiceDetailsFragment()
                5 -> presenter.getProviderDetailsFragment()
                6 -> presenter.getReceiverDetailsFragment()
                7 -> presenter.getFeatureDetailsFragment()
                8 -> presenter.getUsedPermissionDetailsFragment()
                9 -> presenter.getDefinedPermissionDetailsFragment()
                10 -> presenter.getClasspathDetailsFragment()
                else -> null
            }


    override fun getCount() = 11

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> context.resources.getString(R.string.general)
        1 -> context.resources.getString(R.string.certificate)
        2 -> context.resources.getString(R.string.resources)
        3 -> context.resources.getString(R.string.activities)
        4 -> context.resources.getString(R.string.services)
        5 -> context.resources.getString(R.string.content_providers)
        6 -> context.resources.getString(R.string.broadcast_receivers)
        7 -> context.resources.getString(R.string.features)
        8 -> context.resources.getString(R.string.permissions)
        9 -> context.resources.getString(R.string.defined_permissions)
        10 -> context.resources.getString(R.string.classes)
        else -> "TODO"
    }
}