package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.activity

import sk.styk.martin.apkanalyzer.model.detail.ActivityData


/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
class ActivityDetailPagePresenter : ActivityDetailPageContract.Presenter {

    override lateinit var view: ActivityDetailPageContract.View
    private lateinit var activityData: List<ActivityData>

    override fun initialize(data: List<ActivityData>) {
        this.activityData = data
    }

    override fun getData() {
        view.showData()
    }

    override fun itemCount(): Int = activityData.size

    override fun onBindViewOnPosition(position: Int, holder: ActivityDetailPageContract.ItemView) {
        holder.bind(activityData[position])
    }

    override fun runActivity(position: Int) {

        view.startForeignActivity(
                packageName = activityData[position].packageName ?: return,
                activityName = activityData[position].name
        )
    }
}