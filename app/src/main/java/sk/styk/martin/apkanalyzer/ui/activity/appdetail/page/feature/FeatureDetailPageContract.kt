package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.feature

import sk.styk.martin.apkanalyzer.model.detail.FeatureData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.ListDetailPageContract

interface FeatureDetailPageContract {

    interface View : ListDetailPageContract.View

    interface ItemView : ListDetailPageContract.ItemView<FeatureData>

    interface Presenter : ListDetailPageContract.Presenter<FeatureData, View, ItemView>
}