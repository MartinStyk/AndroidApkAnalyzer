package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.feature

import sk.styk.martin.apkanalyzer.model.detail.FeatureData


/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
class FeatureDetailPagePresenter : FeatureDetailPageContract.Presenter {

    override lateinit var view: FeatureDetailPageContract.View
    private lateinit var featureData: List<FeatureData>

    override fun initialize(data: List<FeatureData>) {
        this.featureData = data
    }

    override fun getData() {
        view.showData()
    }

    override fun itemCount(): Int = featureData.size

    override fun onBindViewOnPosition(position: Int, holder: FeatureDetailPageContract.ItemView) {
        holder.bind(featureData[position])
    }

}