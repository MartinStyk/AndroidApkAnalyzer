package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.service

import sk.styk.martin.apkanalyzer.model.detail.ServiceData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.ListDetailPageContract

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface ServiceDetailPageContract {

    interface View : ListDetailPageContract.View

    interface ItemView : ListDetailPageContract.ItemView<ServiceData>

    interface Presenter : ListDetailPageContract.Presenter<ServiceData, View, ItemView>
}