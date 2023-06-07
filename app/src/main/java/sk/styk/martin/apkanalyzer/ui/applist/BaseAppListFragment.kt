package sk.styk.martin.apkanalyzer.ui.applist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialElevationScale
import sk.styk.martin.apkanalyzer.R
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailFragment
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailRequest
import sk.styk.martin.apkanalyzer.util.FragmentTag
import sk.styk.martin.apkanalyzer.util.hideKeyboard

abstract class BaseAppListFragment<VM : BaseAppListViewModel> : Fragment() {

    protected lateinit var viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.appClicked.observe(viewLifecycleOwner) {
            startAppDetail(it)
            view.hideKeyboard()
        }
    }

    private fun startAppDetail(appListClickData: AppListAdapter.AppListClickData) {
        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)
        fragmentManager().beginTransaction().apply {
            appListClickData.view.get()?.let { addSharedElement(it, getString(R.string.transition_app_detail)) }
        }.replace(
            R.id.container,
            AppDetailFragment.newInstance(AppDetailRequest.InstalledPackage(appListClickData.lazyAppListData.packageName)),
            FragmentTag.AppDetailParent.tag,
        )
            .setReorderingAllowed(true)
            .addToBackStack(FragmentTag.AppDetailParent.tag)
            .commit()
    }

    protected open fun fragmentManager() = parentFragmentManager
}
