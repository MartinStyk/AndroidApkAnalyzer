package sk.styk.martin.apkanalyzer.ui.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import sk.styk.martin.apkanalyzer.R

/**
 * Parent fragment for AppListFragment and AppDetailFragment.
 * Purpose of thi fragment is to provide separated screen areas in landscape orientation of w900dp devices
 *
 * @author Martin Styk
 */
class AnalyzeFragment : Fragment() {

    private var rootView: View? = null

    private val isTwoPane: Boolean
        get() = rootView != null && rootView!!.findViewById<View>(R.id.app_detail_container) != null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(R.layout.fragment_analyze, container, false)

        childFragmentManager.beginTransaction().replace(R.id.app_list_container, AppListFragment()).commit()

        return rootView
    }

    fun itemClicked(packageName: String?, pathToPackage: String?) {
        if (isTwoPane) {
            val fragment = AppDetailFragment.create(packageName = packageName, packagePath = pathToPackage)
            childFragmentManager.beginTransaction().replace(R.id.app_detail_container, fragment, AppDetailFragment.TAG).commitAllowingStateLoss()
        } else {
            val activity = AppDetailActivity.createIntent(packageName = packageName, packagePath = pathToPackage, context = context)
            context.startActivity(activity)
        }
    }
}
