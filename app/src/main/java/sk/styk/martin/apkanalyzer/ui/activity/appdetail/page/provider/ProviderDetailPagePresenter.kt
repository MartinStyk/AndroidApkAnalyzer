package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.provider

import sk.styk.martin.apkanalyzer.model.detail.ContentProviderData
import sk.styk.martin.apkanalyzer.util.AppDetailDataExchange

class ProviderDetailPagePresenter : ProviderDetailPageContract.Presenter {

    override lateinit var view: ProviderDetailPageContract.View
    private lateinit var providerData: List<ContentProviderData>

    override fun initialize(packageName : String) {
        this.providerData = AppDetailDataExchange.get(packageName)?.contentProviderData ?: emptyList()
    }

    override fun getData() {
        view.showData()
    }

    override fun itemCount(): Int = providerData.size

    override fun onBindViewOnPosition(position: Int, holder: ProviderDetailPageContract.ItemView) {
        holder.bind(providerData[position])
    }

}