package sk.styk.martin.apkanalyzer.ui.activity.permission.detail.details

import android.os.Bundle
import sk.styk.martin.apkanalyzer.model.detail.PermissionData
import sk.styk.martin.apkanalyzer.ui.BasePresenter

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface PermissionsGeneralDetailsContract {
    interface View {
        fun showPermissionDetails(permissionData: PermissionData, grantedApps: Int, notGrantedApss: Int)
    }

    interface Presenter : BasePresenter<View> {
        fun initialize(bundle: Bundle)
    }
}