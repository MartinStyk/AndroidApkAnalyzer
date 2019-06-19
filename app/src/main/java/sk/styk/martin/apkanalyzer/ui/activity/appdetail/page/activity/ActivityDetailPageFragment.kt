package sk.styk.martin.apkanalyzer.ui.activity.appdetail.page.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_app_detail_activity.*
import sk.styk.martin.apkanalyzer.ApkAnalyzer
import sk.styk.martin.apkanalyzer.R.layout.fragment_app_detail_activity
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract.Companion.ARG_PACKAGE_NAME
import sk.styk.martin.apkanalyzer.util.file.AppOperations

/**
 * @author Martin Styk
 * @version 30.06.2017.
 */
class ActivityDetailPageFragment : Fragment(), ActivityDetailPageContract.View {

    private lateinit var presenter: ActivityDetailPageContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = ActivityDetailPagePresenter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): android.view.View? {
        return inflater.inflate(fragment_app_detail_activity, container, false)
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.initialize(arguments?.getString(ARG_PACKAGE_NAME) ?: throw IllegalArgumentException("data null"))
        presenter.view = this
        presenter.getData()
    }

    override fun showData() {
        recycler_view_activity.adapter = ActivityDetailListAdapter(presenter)
        recycler_view_activity.setHasFixedSize(true)
    }

    override fun startForeignActivity(packageName: String, activityName: String) {
        AppOperations.startForeignActivity(ApkAnalyzer.context, packageName, activityName)
    }
}