package sk.styk.martin.apkanalyzer.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.util.Log
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

    private var fragmentManager: FragmentManager? = null

    private val isTwoPane: Boolean
        get() = rootView != null && rootView!!.findViewById<View>(R.id.app_detail_container) != null

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        rootView = inflater!!.inflate(R.layout.fragment_analyze, container, false)

        val fragment = AppListFragment()
        fragmentManager = childFragmentManager
        fragmentManager!!.beginTransaction().replace(R.id.app_list_container, fragment).commit()

        return rootView
    }

    fun itemClicked(packageName: String, pathToPackage: String) {
        if (!isTwoPane) {
            val context = context
            val intent = Intent(context, AppDetailActivity::class.java)
            intent.putExtra(AppDetailFragment.ARG_PACKAGE_NAME, packageName)
            intent.putExtra(AppDetailFragment.ARG_PACKAGE_PATH, pathToPackage)
            context.startActivity(intent)
        } else {
            val arguments = Bundle()
            arguments.putString(AppDetailFragment.ARG_PACKAGE_NAME, packageName)
            arguments.putString(AppDetailFragment.ARG_PACKAGE_PATH, pathToPackage)
            val fragment = AppDetailFragment()
            fragment.arguments = arguments
            childFragmentManager.beginTransaction().replace(R.id.app_detail_container, fragment, AppDetailFragment.TAG).commitAllowingStateLoss()
        }
    }
}
