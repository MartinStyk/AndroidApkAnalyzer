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
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.string.ClassListDetailPageFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.string.UsedPermissionListDetailPageFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract.Companion.ARG_PACKAGE_NAME
import java.lang.IllegalArgumentException

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
            2 -> ResourceDetailFragment()
            3 -> ActivityDetailPageFragment()
            4 -> ServiceDetailPageFragment()
            5 -> ProviderDetailPageFragment()
            6 -> ReceiverDetailPageFragment()
            7 -> FeatureDetailPageFragment()
            8 -> UsedPermissionListDetailPageFragment()
            9 -> DefinedPermissionListDetailPageFragment()
            10 -> ClassListDetailPageFragment()
            else -> throw IllegalArgumentException()
        }
        fragment.arguments = Bundle().apply {
            putString(ARG_PACKAGE_NAME, presenter.packageName)
        }
        return fragment
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
        else -> throw IllegalArgumentException()
    }
}