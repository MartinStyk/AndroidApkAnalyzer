package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.provider

import sk.styk.martin.apkanalyzer.model.detail.ContentProviderData
import sk.styk.martin.apkanalyzer.model.detail.ServiceData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.ListDetailPageContract

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface ProviderDetailPageContract {

    interface View : ListDetailPageContract.View

    interface ItemView : ListDetailPageContract.ItemView<ContentProviderData>

    interface Presenter : ListDetailPageContract.Presenter<ContentProviderData, View, ItemView>
}