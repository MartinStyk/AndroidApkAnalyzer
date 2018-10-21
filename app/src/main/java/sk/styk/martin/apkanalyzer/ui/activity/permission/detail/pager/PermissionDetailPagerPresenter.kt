package sk.styk.martin.apkanalyzer.ui.activity.permission.detail.pager

import android.content.pm.PackageManager
import android.os.Bundle
import sk.styk.martin.apkanalyzer.ApkAnalyzer.Companion.context
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
class PermissionDetailPagerPresenter : PermissionDetailPagerContract.Presenter {

    override lateinit var view: PermissionDetailPagerContract.View
    override lateinit var localPermissionData: LocalPermissionData

    override fun initialize(bundle: Bundle) {
        localPermissionData = bundle.getParcelable(PermissionDetailPagerFragment.ARG_PERMISSIONS_DATA)
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

}
