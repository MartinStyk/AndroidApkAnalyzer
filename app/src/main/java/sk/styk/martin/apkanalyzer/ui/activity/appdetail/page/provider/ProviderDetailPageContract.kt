package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.provider

import sk.styk.martin.apkanalyzer.model.detail.ContentProviderData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.ListDetailPageContract

interface ProviderDetailPageContract {

    interface View : ListDetailPageContract.View

    interface ItemView : ListDetailPageContract.ItemView<ContentProviderData>

    interface Presenter : ListDetailPageContract.Presenter<ContentProviderData, View, ItemView>
}