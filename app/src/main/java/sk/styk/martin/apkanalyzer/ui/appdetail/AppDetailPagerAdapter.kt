package sk.styk.martin.apkanalyzer.ui.appdetail

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.appdetail.page.activity.AppActivityDetailFragment
import sk.styk.martin.apkanalyzer.ui.appdetail.page.certificate.AppCertificateDetailFragment
import sk.styk.martin.apkanalyzer.ui.appdetail.page.feature.AppFeatureDetailPageFragment
import sk.styk.martin.apkanalyzer.ui.appdetail.page.general.AppGeneralDetailFragment
import sk.styk.martin.apkanalyzer.ui.appdetail.page.provider.AppProviderDetailFragment
import sk.styk.martin.apkanalyzer.ui.appdetail.page.receiver.AppReceiverDetailFragment
import sk.styk.martin.apkanalyzer.ui.appdetail.page.service.AppServiceDetailFragment
import sk.styk.martin.apkanalyzer.ui.appdetail.page.usedpermission.AppDefinedPermissionDetailFragment
import sk.styk.martin.apkanalyzer.ui.appdetail.page.usedpermission.AppUsedPermissionDetailFragment

class AppDetailPagerAdapter(val bundle: Bundle,
                            val context: Context,
                            fm: FragmentManager) : FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int) = when (position) {
        0 -> AppGeneralDetailFragment()
        1 -> AppCertificateDetailFragment()
        2 -> AppUsedPermissionDetailFragment()
        3 -> AppActivityDetailFragment()
        4 -> AppServiceDetailFragment()
        5 -> AppProviderDetailFragment()
        6 -> AppReceiverDetailFragment()
        7 -> AppFeatureDetailPageFragment()
        8 -> AppDefinedPermissionDetailFragment()
        else -> throw IllegalArgumentException()
    }.apply {
        arguments = bundle
    }

    override fun getCount() = 9

    override fun getPageTitle(position: Int): CharSequence = context.resources.getString(
            when (position) {
                0 -> R.string.general
                1 -> R.string.certificate
                2 -> R.string.used_permissions
                3 -> R.string.activities
                4 -> R.string.services
                5 -> R.string.content_providers
                6 -> R.string.broadcast_receivers
                7 -> R.string.features
                8 -> R.string.defined_permissions
                else -> throw IllegalArgumentException()
            })
}