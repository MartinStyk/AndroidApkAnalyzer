package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.receiver

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_app_detail_receiver.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract

class ReceiverDetailPageFragment : Fragment(), ReceiverDetailPageContract.View {

    private lateinit var presenter: ReceiverDetailPageContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ReceiverDetailPagePresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        return inflater.inflate(R.layout.fragment_app_detail_receiver, container, false)
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.initialize(arguments?.getString(AppDetailPagerContract.ARG_PACKAGE_NAME) ?: throw IllegalArgumentException("data null"))
        presenter.view = this
        presenter.getData()
    }

    override fun showData() {
        recycler_view_receiver.adapter = ReceiverDetailListAdapter(presenter)
        recycler_view_receiver.setHasFixedSize(true)
    }

}
