package sk.styk.martin.apkanalyzer.ui.activity.permission.list

import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData
import sk.styk.martin.apkanalyzer.ui.base.BasePresenter
import sk.styk.martin.apkanalyzer.ui.base.ListPresenter

interface LocalPermissionsContract {
    interface View {

        fun changeProgress(currentProgress: Int, maxProgress: Int)

        fun setUpViews()

        fun loadingFinished()

        fun loadingStart()

        fun showPermissionList()

        fun openPermissionDetail(permission: LocalPermissionData)
    }

    interface ItemView {
        var permissionName: String
        var permissionSimpleName: String
        var affectedApps: Int
    }

    interface Presenter : BasePresenter<View>, ListPresenter<ItemView> {

        fun permissionSelected(position: Int)

    }
}