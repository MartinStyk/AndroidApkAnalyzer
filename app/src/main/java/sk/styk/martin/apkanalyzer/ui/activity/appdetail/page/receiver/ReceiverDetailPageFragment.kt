package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.receiver

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_app_detail_receiver.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.model.detail.BroadcastReceiverData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract

/**
 * @author Martin Styk
 * @version 30.06.2017.
 */
class ReceiverDetailPageFragment : Fragment(), ReceiverDetailPageContract.View {

    private lateinit var presenter: ReceiverDetailPageContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ReceiverDetailPagePresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        return inflater.inflate(R.layout.fragment_app_detail_receiver, container, false)
    }

    override fun onViewCreated(view: android.view.View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.initialize(arguments.getParcelableArrayList<BroadcastReceiverData>(AppDetailPagerContract.ARG_PAGER_PAGE)
                ?: throw IllegalArgumentException("data null"))
        presenter.view = this
        presenter.getData()
    }

    override fun showData() {
        recycler_view_receiver.adapter = ReceiverDetailListAdapter(presenter)
        recycler_view_receiver.setHasFixedSize(true)
    }

}
