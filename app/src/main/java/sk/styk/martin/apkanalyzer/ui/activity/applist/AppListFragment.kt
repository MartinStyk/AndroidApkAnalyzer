package sk.styk.martin.apkanalyzer.ui.activity.applist


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_permission_app_list.*
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.business.analysis.task.AppListFromPackageNamesLoader
import sk.styk.martin.apkanalyzer.ui.activity.AppDetailActivity

/**
 * @author Martin Styk
 * @version 15.01.2017
 */
class AppListFragment : Fragment(), AppListContract.View {

    private lateinit var presenter: AppListContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = AppListPresenter(AppListFromPackageNamesLoader(context, arguments.getStringArrayList(AppListContract.PACKAGES_ARGUMENT)), loaderManager)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_permission_app_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.view = this
        presenter.initialize(arguments)
    }

    override fun setUpViews() {
        recycler_view_app_list.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }

    override fun hideLoading() {
        list_view_progress_bar.visibility = View.GONE
    }

    override fun nothingToDisplay() {
        list_view_progress_bar.visibility = View.GONE
        nothing_to_show.visibility = View.VISIBLE
    }

    override fun showAppList() {
        list_view_progress_bar.visibility = View.GONE
        recycler_view_app_list.adapter = AppListRecyclerAdapter(presenter)
    }

    override fun openAppDetailActivity(packageName: String) {
        context.startActivity(AppDetailActivity.createIntent(packageName, null, context))
    }
}
