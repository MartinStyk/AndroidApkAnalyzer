package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.provider

import sk.styk.martin.apkanalyzer.model.detail.ContentProviderData


/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
class ProviderDetailPagePresenter : ProviderDetailPageContract.Presenter {

    override lateinit var view: ProviderDetailPageContract.View
    private lateinit var providerData: List<ContentProviderData>

    override fun initialize(data: List<ContentProviderData>) {
        this.providerData = data
    }

    override fun getData() {
        view.showData()
    }

    override fun itemCount(): Int = providerData.size

    override fun onBindViewOnPosition(position: Int, holder: ProviderDetailPageContract.ItemView) {
        holder.bind(providerData[position])
    }

}