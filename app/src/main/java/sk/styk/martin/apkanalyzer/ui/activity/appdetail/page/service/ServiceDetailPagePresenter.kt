package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.service

import sk.styk.martin.apkanalyzer.model.detail.ServiceData
import sk.styk.martin.apkanalyzer.util.AppDetailDataExchange


/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
class ServiceDetailPagePresenter : ServiceDetailPageContract.Presenter {

    override lateinit var view: ServiceDetailPageContract.View
    private lateinit var serviceData: List<ServiceData>

    override fun initialize(packageName : String) {
        this.serviceData = AppDetailDataExchange.get(packageName)?.serviceData ?: emptyList()
    }

    override fun getData() {
        view.showData()
    }

    override fun itemCount(): Int = serviceData.size

    override fun onBindViewOnPosition(position: Int, holder: ServiceDetailPageContract.ItemView) {
        holder.bind(serviceData[position])
    }

}