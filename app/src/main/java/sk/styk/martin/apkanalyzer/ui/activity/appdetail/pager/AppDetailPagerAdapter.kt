package sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
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
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.string.StringListDetailPageFragment
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract.Companion.ARG_PAGER_PAGE
import java.util.*

/**
 * @author Martin Styk
 * @version 18.06.2017.
 */
class AppDetailPagerAdapter(
        private val context: Context,
        fm: FragmentManager,
        private val presenter: AppDetailPagerContract.Presenter) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        val fragment: Fragment
        val args = Bundle()
        when (position) {
            0 -> {
                args.putParcelable(ARG_PAGER_PAGE, presenter.getData()?.generalData)
                fragment = GeneralDetailFragment()
            }

            1 -> {
                args.putParcelable(ARG_PAGER_PAGE, presenter.getData()?.certificateData)
                fragment = CertificateDetailFragment()
            }

            2 -> {
                args.putParcelable(ARG_PAGER_PAGE, presenter.getData()?.resourceData)
                fragment = ResourceDetailFragment()
            }

            3 -> {
                args.putParcelableArrayList(ARG_PAGER_PAGE, presenter.getData()?.activityData as ArrayList<out Parcelable>)
                fragment = ActivityDetailPageFragment()
            }

            4 -> {
                args.putParcelableArrayList(ARG_PAGER_PAGE, presenter.getData()?.serviceData as ArrayList<out Parcelable>)
                fragment = ServiceDetailPageFragment()
            }

            5 -> {
                args.putParcelableArrayList(ARG_PAGER_PAGE, presenter.getData()?.contentProviderData as ArrayList<out Parcelable>)
                fragment = ProviderDetailPageFragment()
            }

            6 -> {
                args.putParcelableArrayList(ARG_PAGER_PAGE, presenter.getData()?.broadcastReceiverData as ArrayList<out Parcelable>)
                fragment = ReceiverDetailPageFragment()
            }

            7 -> {
                args.putParcelableArrayList(ARG_PAGER_PAGE, presenter.getData()?.featureData as ArrayList<out Parcelable>)
                fragment = FeatureDetailPageFragment()
            }

            8 -> {
                args.putStringArrayList(ARG_PAGER_PAGE, presenter.getData()?.permissionData?.usesPermissionsNames as ArrayList<String>)
                fragment = StringListDetailPageFragment()
            }

            9 -> {
                args.putStringArrayList(ARG_PAGER_PAGE, presenter.getData()?.permissionData?.definesPermissionsNames as ArrayList<String>)
                fragment = StringListDetailPageFragment()
            }

            10 -> {
                args.putStringArrayList(ARG_PAGER_PAGE, presenter.getData()?.classPathData?.allClasses  as ArrayList<String>)
                fragment = StringListDetailPageFragment()
            }

            else -> return null
        }
        fragment.arguments = args
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
        else -> "TODO"
    }
}