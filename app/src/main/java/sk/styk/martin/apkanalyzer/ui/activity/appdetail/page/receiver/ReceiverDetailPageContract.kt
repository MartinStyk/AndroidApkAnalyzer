package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.receiver

import sk.styk.martin.apkanalyzer.model.detail.BroadcastReceiverData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.ListDetailPageContract

interface ReceiverDetailPageContract {

    interface View : ListDetailPageContract.View

    interface ItemView : ListDetailPageContract.ItemView<BroadcastReceiverData>

    interface Presenter : ListDetailPageContract.Presenter<BroadcastReceiverData, View, ItemView>
}