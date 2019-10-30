package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.feature

import sk.styk.martin.apkanalyzer.model.detail.FeatureData
import sk.styk.martin.apkanalyzer.util.AppDetailDataExchange

class FeatureDetailPagePresenter : FeatureDetailPageContract.Presenter {

    override lateinit var view: FeatureDetailPageContract.View
    private lateinit var featureData: List<FeatureData>

    override fun initialize(packageName : String) {
        this.featureData = AppDetailDataExchange.get(packageName)?.featureData ?: emptyList()
    }

    override fun getData() {
        view.showData()
    }

    override fun itemCount(): Int = featureData.size

    override fun onBindViewOnPosition(position: Int, holder: FeatureDetailPageContract.ItemView) {
        holder.bind(featureData[position])
    }

}