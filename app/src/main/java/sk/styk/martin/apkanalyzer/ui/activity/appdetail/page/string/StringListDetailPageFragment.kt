package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.string

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_app_detail_simple_string_list.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract
import sk.styk.martin.apkanalyzer.ui.adapter.detaillist.SimpleStringListAdapter

/**
 * @author Martin Styk
 * @version 30.06.2017.
 */
class StringListDetailPageFragment : Fragment(), StringListDetailPageContract.View {

    private lateinit var presenter: StringListDetailPageContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = StringListDetailPagePresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        return inflater.inflate(R.layout.fragment_app_detail_simple_string_list, container, false)
    }

    override fun onViewCreated(view: android.view.View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.initialize(arguments.getStringArrayList((AppDetailPagerContract.ARG_PAGER_PAGE)))
        presenter.view = this
        presenter.getData()
    }

    override fun showData() {
        recycler_view_simple_string_list.adapter = SimpleStringListAdapter(presenter)
        recycler_view_simple_string_list.setHasFixedSize(true)
        recycler_view_simple_string_list.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

}
