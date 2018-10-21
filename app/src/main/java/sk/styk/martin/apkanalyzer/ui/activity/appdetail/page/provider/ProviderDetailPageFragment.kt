package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.provider

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract

/**
 * @author Martin Styk
 * @version 30.06.2017.
 */
class ProviderDetailPageFragment : Fragment(), ProviderDetailPageContract.View {

    private lateinit var presenter: ProviderDetailPageContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ProviderDetailPagePresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        return inflater.inflate(R.layout.fragment_app_detail_provider, container, false)
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.initialize(arguments?.getParcelableArrayList(AppDetailPagerContract.ARG_PAGER_PAGE)
                ?: throw IllegalArgumentException("data null"))
        presenter.view = this
        presenter.getData()
    }

    override fun showData() {
        recycler_view_provider.adapter = ProviderDetailListAdapter(presenter)
        recycler_view_provider.setHasFixedSize(true)
    }

}
