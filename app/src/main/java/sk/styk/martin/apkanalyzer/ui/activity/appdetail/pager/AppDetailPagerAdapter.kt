package sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.activity.ActivityDetailPageFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.feature.FeatureDetailPageFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.itemized.CertificateDetailFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.itemized.GeneralDetailFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.itemized.ResourceDetailFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.provider.ProviderDetailPageFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.receiver.ReceiverDetailPageFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.service.ServiceDetailPageFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.string.DefinedPermissionListDetailPageFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.string.UsedPermissionListDetailPageFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract.Companion.ARG_PACKAGE_NAME

/**
 * @author Martin Styk
 * @version 18.06.2017.
 */
class AppDetailPagerAdapter(
        private val context: Context,
        fm: FragmentManager,
        private val presenter: AppDetailPagerContract.Presenter) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        val fragment: Fragment = when (position) {
            0 -> GeneralDetailFragment()
            1 -> CertificateDetailFragment()
            2 -> UsedPermissionListDetailPageFragment()
            3 -> ActivityDetailPageFragment()
            4 -> ServiceDetailPageFragment()
            5 -> ProviderDetailPageFragment()
            6 -> ReceiverDetailPageFragment()
            7 -> FeatureDetailPageFragment()
            8 -> DefinedPermissionListDetailPageFragment()
            9 -> ResourceDetailFragment()
            else -> throw IllegalArgumentException()
        }
        fragment.arguments = Bundle().apply {
            putString(ARG_PACKAGE_NAME, presenter.packageName)
        }
        return fragment
    }

    override fun getCount() = 10

    override fun getPageTitle(position: Int): CharSequence = context.resources.getString(
            when (position) {
                0 -> R.string.general
                1 -> R.string.certificate
                2 -> R.string.permissions
                3 -> R.string.activities
                4 -> R.string.services
                5 -> R.string.content_providers
                6 -> R.string.broadcast_receivers
                7 -> R.string.features
                8 -> R.string.defined_permissions
                9 -> R.string.resources
                else -> throw IllegalArgumentException()
            })
}