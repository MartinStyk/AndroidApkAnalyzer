package sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_app_detail.*
import sk.styk.martin.apkanalyzer.business.analysis.task.AppDetailLoader
import sk.styk.martin.apkanalyzer.model.detail.AppDetailData
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.actions.AppActionsDialog
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract.Companion.ARG_PACKAGE_NAME
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerContract.Companion.ARG_PACKAGE_PATH

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a [MainActivity]
 * in two-pane mode (on tablets) or a [AppDetailActivity]
 * on handsets.
 *
 * @author Martin Styk
 */
class AppDetailPagerFragment : Fragment(), AppDetailPagerContract.View {

    private lateinit var adapter: AppDetailPagerAdapter
    lateinit var presenter: AppDetailPagerContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = AppDetailPagerPresenter(AppDetailLoader(context = requireContext(),
                packageName = arguments?.getString(ARG_PACKAGE_NAME),
                pathToPackage = arguments?.getString(ARG_PACKAGE_PATH)), loaderManager)
        adapter = AppDetailPagerAdapter(requireContext(), fragmentManager!!, presenter)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_app_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.view = this
        presenter.initialize(arguments!!)
    }

    override fun setUpViews() {
        pager.adapter = adapter
        tabs.setupWithViewPager(pager)
        // if we are in 2-pane mode initialize floating button
        btn_actions.setOnClickListener { presenter.actionButtonClick() }
    }

    override fun hideLoading() {
        item_detail_loading.visibility = View.GONE
    }

    override fun showLoadingFailed() {
        item_detail_error.visibility = View.VISIBLE
        activity?.toolbar_layout?.title = getString(R.string.loading_failed)
    }

    override fun showAppDetails(packageName: String, icon: Drawable?) {
        activity?.toolbar_layout?.title = packageName
        activity?.toolbar_layout_image?.setImageDrawable(icon)

        pager.visibility = View.VISIBLE
    }

    override fun showActionDialog(data: AppDetailData) {
        AppActionsDialog.newInstance(data)
                .show((context as AppCompatActivity).supportFragmentManager, AppActionsDialog::class.java.simpleName)
    }

    companion object {

        val TAG = AppDetailPagerFragment::class.java.simpleName

        fun create(packageName: String? = null, packagePath: String? = null): AppDetailPagerFragment {
            val arguments = Bundle()
            arguments.putString(ARG_PACKAGE_PATH, packagePath)
            arguments.putString(ARG_PACKAGE_NAME, packageName)
            val detailFragment = AppDetailPagerFragment()
            detailFragment.arguments = arguments

            return detailFragment
        }
    }
}
