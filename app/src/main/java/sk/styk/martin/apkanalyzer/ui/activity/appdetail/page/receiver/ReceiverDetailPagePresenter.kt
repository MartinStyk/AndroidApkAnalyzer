package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.receiver

import sk.styk.martin.apkanalyzer.model.detail.BroadcastReceiverData
import sk.styk.martin.apkanalyzer.util.AppDetailDataExchange

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
class ReceiverDetailPagePresenter : ReceiverDetailPageContract.Presenter {

    override lateinit var view: ReceiverDetailPageContract.View
    private lateinit var receiverData: List<BroadcastReceiverData>

    override fun initialize(packageName : String) {
        this.receiverData = AppDetailDataExchange.get(packageName)?.broadcastReceiverData ?: emptyList()
    }

    override fun getData() {
        view.showData()
    }

    override fun itemCount(): Int = receiverData.size

    override fun onBindViewOnPosition(position: Int, holder: ReceiverDetailPageContract.ItemView) {
        holder.bind(receiverData[position])
    }

}