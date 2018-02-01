package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page

import sk.styk.martin.apkanalyzer.ui.base.BasePresenter
import sk.styk.martin.apkanalyzer.ui.base.ListPresenter

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface ListDetailPageContract {

    interface View {
        fun showData()
    }

    interface ItemView<in DATA> {
        fun bind(item: DATA)
    }

    interface Presenter<in DATA, VIEW, in ITEM> : BasePresenter<VIEW>, ListPresenter<ITEM> {
        fun initialize(data: List<DATA>)

        fun getData()
    }
}