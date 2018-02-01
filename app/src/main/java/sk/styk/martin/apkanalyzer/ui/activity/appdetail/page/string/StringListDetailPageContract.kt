package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.string

import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.ListDetailPageContract

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface StringListDetailPageContract {

    interface View : ListDetailPageContract.View

    interface ItemView : ListDetailPageContract.ItemView<String>

    interface Presenter : ListDetailPageContract.Presenter<String, View, ItemView>
}