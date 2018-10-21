package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract.Companion.ARG_PAGER_PAGE
import sk.styk.martin.apkanalyzer.ui.base.BasePresenter

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface AppDetailPageContract<DATA : Parcelable, BINDING : ViewDataBinding> {

    abstract class View<DATA : Parcelable, BINDING : ViewDataBinding> : Fragment() {

        protected lateinit var binding: BINDING
        protected lateinit var presenter: AppDetailPageContract.Presenter<DATA, BINDING>

        @LayoutRes
        open val layout: Int = R.layout.fragment_app_detail_resource

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            presenter = AppDetailPageContract.Presenter()
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
            binding = DataBindingUtil.inflate(inflater, layout, container, false)
            return binding.root
        }

        override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            presenter.initialize(extractData())
            presenter.view = this
            presenter.getData()
        }

        open fun extractData(): DATA {
            return arguments?.getParcelable(ARG_PAGER_PAGE)
                    ?: throw IllegalArgumentException("data null")
        }

        abstract fun showData(data: DATA)
    }

    open class Presenter<DATA : Parcelable, BINDING : ViewDataBinding> : BasePresenter<View<DATA, BINDING>> {
        override lateinit var view: View<DATA, BINDING>
        lateinit var data: DATA

        fun initialize(data: DATA) {
            this.data = data
        }

        fun getData() {
            view.showData(data)
        }
    }
}
