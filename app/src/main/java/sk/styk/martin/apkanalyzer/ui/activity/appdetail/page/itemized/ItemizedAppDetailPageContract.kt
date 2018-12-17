package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.itemized

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract.Companion.ARG_PACKAGE_NAME
import sk.styk.martin.apkanalyzer.ui.base.BasePresenter
import sk.styk.martin.apkanalyzer.util.AppDetailDataExchange

/**
 * @author Martin Styk
 * @version 28.01.2018.
 */
interface ItemizedAppDetailPageContract<DATA : Parcelable, BINDING : ViewDataBinding> {

    abstract class View<DATA : Parcelable, BINDING : ViewDataBinding> : Fragment() {

        protected lateinit var binding: BINDING
        protected lateinit var presenter: Presenter<DATA, BINDING>

        @LayoutRes
        open val layout: Int = R.layout.fragment_app_detail_resource

        abstract val itemizedDataType: ItemizedDataType

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            presenter = Presenter<DATA, BINDING>()
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
            binding = DataBindingUtil.inflate(inflater, layout, container, false)
            return binding.root
        }

        override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            presenter.initialize(arguments?.getString(ARG_PACKAGE_NAME)
                    ?: throw IllegalArgumentException("data null"), itemizedDataType)
            presenter.view = this
            presenter.getData()
        }

        abstract fun showData(data: DATA)
    }

    open class Presenter<DATA : Parcelable, BINDING : ViewDataBinding> : BasePresenter<View<DATA, BINDING>> {
        override lateinit var view: View<DATA, BINDING>
        lateinit var data: DATA

        fun initialize(packageName: String, itemizedDataType: ItemizedDataType) {
            val data = AppDetailDataExchange.require(packageName)
            this.data = when (itemizedDataType) {
                ItemizedDataType.CERTIFICATE_DATA -> data.certificateData as DATA
                ItemizedDataType.GENERAL_DATA -> data.generalData as DATA
                ItemizedDataType.RESOURCE_DATA -> data.resourceData as DATA
            }
        }

        fun getData() {
            view.showData(data)
        }
    }

}