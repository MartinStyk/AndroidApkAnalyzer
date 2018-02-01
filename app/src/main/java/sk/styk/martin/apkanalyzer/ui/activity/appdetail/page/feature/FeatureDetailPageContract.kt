package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.feature

import sk.styk.martin.apkanalyzer.model.detail.FeatureData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.ListDetailPageContract

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface FeatureDetailPageContract {

    interface View : ListDetailPageContract.View

    interface ItemView : ListDetailPageContract.ItemView<FeatureData>

    interface Presenter : ListDetailPageContract.Presenter<FeatureData, View, ItemView>
}