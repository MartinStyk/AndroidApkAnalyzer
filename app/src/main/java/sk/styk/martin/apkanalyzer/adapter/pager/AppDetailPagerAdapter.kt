package sk.styk.martin.apkanalyzer.adapter.pager

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.activity.AppDetailFragment
import sk.styk.martin.apkanalyzer.activity.detailfragment.AppDetailFragment_Activity
import sk.styk.martin.apkanalyzer.activity.detailfragment.AppDetailFragment_Certificate
import sk.styk.martin.apkanalyzer.activity.detailfragment.AppDetailFragment_Classes
import sk.styk.martin.apkanalyzer.activity.detailfragment.AppDetailFragment_Feature
import sk.styk.martin.apkanalyzer.activity.detailfragment.AppDetailFragment_General
import sk.styk.martin.apkanalyzer.activity.detailfragment.AppDetailFragment_Permission
import sk.styk.martin.apkanalyzer.activity.detailfragment.AppDetailFragment_Provider
import sk.styk.martin.apkanalyzer.activity.detailfragment.AppDetailFragment_Receiver
import sk.styk.martin.apkanalyzer.activity.detailfragment.AppDetailFragment_Resource
import sk.styk.martin.apkanalyzer.activity.detailfragment.AppDetailFragment_Service
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import java.util.*

/**
 * @author Martin Styk
 * @version 18.06.2017.
 */
class AppDetailPagerAdapter(private val context: Context, fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    private var data: AppDetailData? = null

    override fun getItem(position: Int): Fragment? {
        val fragment: Fragment
        val args = Bundle()
        when (position) {
            0 -> {
                args.putParcelable(AppDetailFragment.ARG_CHILD, data!!.generalData)
                fragment = AppDetailFragment_General()
            }

            1 -> {
                args.putParcelable(AppDetailFragment.ARG_CHILD, data!!.certificateData)
                fragment = AppDetailFragment_Certificate()
            }

            2 -> {
                args.putParcelable(AppDetailFragment.ARG_CHILD, data!!.resourceData)
                fragment = AppDetailFragment_Resource()
            }

            3 -> {
                args.putParcelableArrayList(AppDetailFragment.ARG_CHILD, data!!.activityData as ArrayList<out Parcelable>)
                fragment = AppDetailFragment_Activity()
            }

            4 -> {
                args.putParcelableArrayList(AppDetailFragment.ARG_CHILD, data!!.serviceData as ArrayList<out Parcelable>)
                fragment = AppDetailFragment_Service()
            }

            5 -> {
                args.putParcelableArrayList(AppDetailFragment.ARG_CHILD, data!!.contentProviderData as ArrayList<out Parcelable>)
                fragment = AppDetailFragment_Provider()
            }

            6 -> {
                args.putParcelableArrayList(AppDetailFragment.ARG_CHILD, data!!.broadcastReceiverData as ArrayList<out Parcelable>)
                fragment = AppDetailFragment_Receiver()
            }

            7 -> {
                args.putParcelableArrayList(AppDetailFragment.ARG_CHILD, data!!.featureData as ArrayList<out Parcelable>)
                fragment = AppDetailFragment_Feature()
            }

            8 -> {
                args.putStringArrayList(AppDetailFragment.ARG_CHILD, data!!.permissionData.usesPermissionsNames as ArrayList<String>)
                fragment = AppDetailFragment_Permission()
            }

            9 -> {
                args.putStringArrayList(AppDetailFragment.ARG_CHILD, data!!.permissionData.definesPermissionsNames as ArrayList<String>)
                fragment = AppDetailFragment_Permission()
            }

            10 -> {
                args.putParcelable(AppDetailFragment.ARG_CHILD, data!!.classPathData)
                fragment = AppDetailFragment_Classes()
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

    fun dataChange(data: AppDetailData) {
        this.data = data
    }

}