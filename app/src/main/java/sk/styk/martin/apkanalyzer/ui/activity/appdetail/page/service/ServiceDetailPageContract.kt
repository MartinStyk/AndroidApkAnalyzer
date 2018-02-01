package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.service

import sk.styk.martin.apkanalyzer.model.detail.ActivityData
import sk.styk.martin.apkanalyzer.ui.BasePresenter

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface ServiceDetailPageContract {

    interface View {
        fun showData()

        fun startForeignActivity(packageName: String, activityName: String)
    }

    interface ItemView {
        fun bind(item: ActivityData)
    }

    interface Presenter : BasePresenter<View> {
        fun initialize(activityData: List<ActivityData>)

        fun getData()

        fun activityCount(): Int

        fun onBindViewOnPosition(position: Int, holder: ItemView)

        fun runActivity(position: Int)
    }
}