package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.itemized

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract.Companion.ARG_PACKAGE_NAME
import sk.styk.martin.apkanalyzer.ui.base.BasePresenter
import sk.styk.martin.apkanalyzer.util.AppDetailDataExchange

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
            presenter.provideData()
        }

        abstract fun showData(data: DATA)

        abstract fun showNoData()
    }

    open class Presenter<DATA : Parcelable, BINDING : ViewDataBinding> : BasePresenter<View<DATA, BINDING>> {
        override lateinit var view: View<DATA, BINDING>
        private var data: Parcelable? = null

        fun initialize(packageName: String, itemizedDataType: ItemizedDataType) {
            AppDetailDataExchange.get (packageName)?.let {
                data = when (itemizedDataType) {
                    ItemizedDataType.CERTIFICATE_DATA -> it.certificateData
                    ItemizedDataType.GENERAL_DATA -> it.generalData
                    ItemizedDataType.RESOURCE_DATA -> it.resourceData
                }
            }
        }

        @Suppress("UNCHECKED_CAST")
        fun provideData() {
            if (data != null)
                view.showData(data as DATA)
            else
                view.showNoData()
        }
    }

}