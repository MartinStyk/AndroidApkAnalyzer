package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.string

import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.ListDetailPageContract

interface StringListDetailPageContract {

    interface View : ListDetailPageContract.View

    interface ItemView : ListDetailPageContract.ItemView<String>

    interface Presenter : ListDetailPageContract.Presenter<String, View, ItemView>
}