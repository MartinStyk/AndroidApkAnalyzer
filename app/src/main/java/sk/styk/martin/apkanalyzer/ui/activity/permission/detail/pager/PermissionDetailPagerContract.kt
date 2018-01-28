package sk.styk.martin.apkanalyzer.ui.activity.permission.detail.pager

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData
import sk.styk.martin.apkanalyzer.ui.BasePresenter

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface PermissionDetailPagerContract {
    interface View {
        fun setUpViews()
    }

    interface Presenter : BasePresenter {
        val localPermissionData: LocalPermissionData

        fun initialize(bundle: Bundle)

        fun loadPermissionDescription(packageManager: PackageManager): String

        fun getGeneralDetailsFragment(): Fragment

        fun getGrantedAppsFragment(): Fragment

        fun getNotGrantedAppsFragment(): Fragment

    }
}