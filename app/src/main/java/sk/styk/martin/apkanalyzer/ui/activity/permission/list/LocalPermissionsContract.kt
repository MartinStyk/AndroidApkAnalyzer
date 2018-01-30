package sk.styk.martin.apkanalyzer.ui.activity.permission.list

import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData
import sk.styk.martin.apkanalyzer.ui.BasePresenter

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface LocalPermissionsContract {
    interface View {

        fun changeProgress(currentProgress: Int, maxProgress: Int)

        fun setUpViews()

        fun loadingFinished()

        fun loadingStart()

        fun showPermissionList()

        fun openPermissionDetail(permission : LocalPermissionData)
    }

    interface ItemView {
        var permissionName: String
        var permissionSimpleName: String
        var affectedApps: Int
    }

    interface Presenter : BasePresenter<View> {

        fun permissionCount(): Int

        fun permissionSelected(position: Int)

        fun onBindPermissionViewOnPosition(position: Int, holder: ItemView)
    }
}