package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.activity

import sk.styk.martin.apkanalyzer.model.detail.ActivityData
import sk.styk.martin.apkanalyzer.ui.ListPresenter
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.ListDetailPageContract

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface ActivityDetailPageContract {

    interface View : ListDetailPageContract.View {
        fun startForeignActivity(packageName: String, activityName: String)
    }

    interface ItemView : ListDetailPageContract.ItemView<ActivityData>

    interface Presenter : ListDetailPageContract.Presenter<ActivityData,View, ItemView>{
        fun runActivity(position: Int)
    }
}