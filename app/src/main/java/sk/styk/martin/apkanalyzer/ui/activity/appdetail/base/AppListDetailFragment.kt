package sk.styk.martin.apkanalyzer.ui.activity.appdetail.base

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.analytics.FirebaseAnalytics
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.pager.AppDetailPagerFragment
import sk.styk.martin.apkanalyzer.ui.activity.applist.searchable.AppListFragment

/**
 * Parent fragment for AppListFragment and AppDetailPagerFragment.
 * Purpose of thi fragment is to provide separated screen areas in landscape orientation of w900dp devices
 *
 * @author Martin Styk
 */
class AppListDetailFragment : Fragment() {

    private var rootView: View? = null

    private val isTwoPane: Boolean
        get() = rootView != null && rootView!!.findViewById<View>(R.id.app_detail_container) != null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_analyze, container, false)
    }

    override fun onResume() {
        super.onResume()
        FirebaseAnalytics.getInstance(requireContext())
                .setCurrentScreen(requireActivity(), AppListDetailFragment::class.java.simpleName, AppListDetailFragment::class.java.simpleName)
    }

    fun itemClicked(packageName: String?) {
        if (isTwoPane) {
            val fragment = AppDetailPagerFragment.create(packageName = packageName)
            childFragmentManager.beginTransaction().replace(R.id.app_detail_container, fragment, AppDetailPagerFragment.TAG).commitAllowingStateLoss()
        } else {
            val activity = AppDetailActivity.createIntent(packageName = packageName, context = requireContext())
            requireContext().startActivity(activity)
        }
    }

    fun itemSelectedFromFile(fileUri : Uri) {
        if (isTwoPane) {
            val fragment = AppDetailPagerFragment.create(packageUri = fileUri)
            childFragmentManager.beginTransaction().replace(R.id.app_detail_container, fragment, AppDetailPagerFragment.TAG).commitAllowingStateLoss()
        } else {
            val activity = AppDetailActivity.createIntent(packageUri = fileUri, context = requireContext())
            requireContext().startActivity(activity)
        }
    }
}
