package sk.styk.martin.apkanalyzer.ui.activity.permission.detail.details

import android.os.Bundle
import sk.styk.martin.apkanalyzer.ui.activity.permission.detail.pager.PermissionDetailPagerFragment


/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
class PermissionsGeneralDetailsPresenter : PermissionsGeneralDetailsContract.Presenter {

    override lateinit var view: PermissionsGeneralDetailsContract.View

    override fun initialize(bundle: Bundle) {
        view.showPermissionDetails(
                permissionData = bundle.getParcelable(PermissionDetailPagerFragment.ARG_CHILD)
                        ?: throw IllegalArgumentException("data null"),
                grantedApps = bundle.getInt(PermissionsGeneralDetailsFragment.ARG_NUMBER_GRANTED_APPS),
                notGrantedApss = bundle.getInt(PermissionsGeneralDetailsFragment.ARG_NUMBER_NOT_GRANTED_APPS)
        )
    }
}