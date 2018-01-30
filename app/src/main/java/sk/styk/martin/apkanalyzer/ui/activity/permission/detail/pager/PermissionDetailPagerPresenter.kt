package sk.styk.martin.apkanalyzer.ui.activity.permission.detail.pager

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import sk.styk.martin.apkanalyzer.ApkAnalyzer.Companion.context
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData
import sk.styk.martin.apkanalyzer.ui.activity.applist.AppListContract.Companion.PACKAGES_ARGUMENT
import sk.styk.martin.apkanalyzer.ui.activity.applist.AppListFragment
import sk.styk.martin.apkanalyzer.ui.activity.permission.detail.details.PermissionsGeneralDetailsFragment


/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
class PermissionDetailPagerPresenter : PermissionDetailPagerContract.Presenter {

    override lateinit var view: PermissionDetailPagerContract.View
    override lateinit var localPermissionData: LocalPermissionData

    override fun initialize(bundle: Bundle) {
        localPermissionData = bundle.getParcelable<LocalPermissionData>(PermissionDetailPagerFragment.ARG_PERMISSIONS_DATA)
                ?: throw IllegalArgumentException("data null")

        view.setUpViews()
    }

    override fun loadPermissionDescription(packageManager: PackageManager): String {
        var desc: CharSequence? = null
        try {
            desc = packageManager.getPermissionInfo(localPermissionData.permissionData.name, PackageManager.GET_META_DATA).loadDescription(packageManager)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return if (desc == null) context.getString(R.string.NA) else desc.toString()
    }

    override fun getGeneralDetailsFragment(): Fragment {
        val args = Bundle()
        args.putParcelable(PermissionDetailPagerFragment.ARG_CHILD, localPermissionData.permissionData)
        args.putInt(PermissionsGeneralDetailsFragment.ARG_NUMBER_GRANTED_APPS, localPermissionData.grantedPackageNames.size)
        args.putInt(PermissionsGeneralDetailsFragment.ARG_NUMBER_NOT_GRANTED_APPS, localPermissionData.notGrantedPackageNames.size)
        val fragment = PermissionsGeneralDetailsFragment()
        fragment.arguments = args
        return fragment
    }

    override fun getGrantedAppsFragment(): Fragment {
        val args = Bundle()
        args.putStringArrayList(PACKAGES_ARGUMENT, localPermissionData.grantedPackageNames as ArrayList<String>)
        val fragment = AppListFragment()
        fragment.arguments = args
        return fragment
    }

    override fun getNotGrantedAppsFragment(): Fragment {
        val args = Bundle()
        args.putStringArrayList(PACKAGES_ARGUMENT, localPermissionData.notGrantedPackageNames as ArrayList<String>)
        val fragment = AppListFragment()
        fragment.arguments = args
        return fragment
    }
}
