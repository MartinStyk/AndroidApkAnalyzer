package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.service

import sk.styk.martin.apkanalyzer.model.detail.ActivityData
import sk.styk.martin.apkanalyzer.model.detail.ServiceData


/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
class ServiceDetailPagePresenter : ServiceDetailPageContract.Presenter {

    override lateinit var view: ServiceDetailPageContract.View
    private lateinit var serviceData: List<ServiceData>

    override fun initialize(data: List<ServiceData>) {
        this.serviceData = data
    }

    override fun getData() {
        view.showData()
    }

    override fun itemCount(): Int = serviceData.size

    override fun onBindViewOnPosition(position: Int, holder: ServiceDetailPageContract.ItemView) {
        holder.bind(serviceData[position])
    }

}