package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.service

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_app_detail_service.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.model.detail.ServiceData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract

/**
 * @author Martin Styk
 * @version 30.06.2017.
 */
class ServiceDetailPageFragment : Fragment(), ServiceDetailPageContract.View {

    private lateinit var presenter: ServiceDetailPageContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ServiceDetailPagePresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        return inflater.inflate(R.layout.fragment_app_detail_service, container, false)
    }

    override fun onViewCreated(view: android.view.View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.initialize(arguments.getParcelableArrayList<ServiceData>(AppDetailPagerContract.ARG_PAGER_PAGE)
                ?: throw IllegalArgumentException("data null"))
        presenter.view = this
        presenter.getData()
    }

    override fun showData() {
        recycler_view_service.adapter = ServiceDetailListAdapter(presenter)
        recycler_view_service.setHasFixedSize(true)
    }

}
