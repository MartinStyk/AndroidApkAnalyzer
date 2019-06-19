package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.string

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_app_detail_simple_string_list.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract

/**
 * @author Martin Styk
 * @version 30.06.2017.
 */
abstract class StringListDetailPageFragment : Fragment(), StringListDetailPageContract.View {

    private lateinit var presenter: StringListDetailPageContract.Presenter

    abstract val stringDataType: StringListDetailPagePresenter.StringDataType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = StringListDetailPagePresenter(stringDataType)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        return inflater.inflate(R.layout.fragment_app_detail_simple_string_list, container, false)
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.initialize(arguments?.getString(AppDetailPagerContract.ARG_PACKAGE_NAME) ?: throw IllegalArgumentException("data null"))
        presenter.view = this
        presenter.getData()
    }

    override fun showData() {
        recycler_view_simple_string_list.adapter = SimpleStringListAdapter(presenter)
        recycler_view_simple_string_list.setHasFixedSize(true)
        recycler_view_simple_string_list.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

}
