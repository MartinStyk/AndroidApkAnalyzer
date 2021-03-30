package sk.styk.martin.apkanalyzer.ui.appdetail.page

import sk.styk.martin.apkanalyzer.ui.base.BasePresenter
import sk.styk.martin.apkanalyzer.ui.base.ListPresenter

interface ListDetailPageContract {

    interface View {
        fun showData()
    }

    interface ItemView<in DATA> {
        fun bind(item: DATA)
    }

    interface Presenter<in DATA, VIEW, in ITEM> : BasePresenter<VIEW>, ListPresenter<ITEM> {
        fun initialize(packageName: String)

        fun getData()
    }
}