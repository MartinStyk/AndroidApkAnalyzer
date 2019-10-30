package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.service

import sk.styk.martin.apkanalyzer.model.detail.ServiceData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.ListDetailPageContract

interface ServiceDetailPageContract {

    interface View : ListDetailPageContract.View

    interface ItemView : ListDetailPageContract.ItemView<ServiceData>

    interface Presenter : ListDetailPageContract.Presenter<ServiceData, View, ItemView>
}