package sk.styk.martin.apkanalyzer.ui.applist


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import sk.styk.martin.apkanalyzer.model.list.AppListData
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailActivity
import sk.styk.martin.apkanalyzer.ui.appdetail.AppDetailRequest

abstract class BaseAppListFragment<VM : BaseAppListViewModel> : Fragment() {

    protected lateinit var viewModel: VM

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.appClicked.observe(viewLifecycleOwner, { startAppDetail(it) })
    }

    private fun startAppDetail(appListData: AppListData) {
        val intent = Intent(requireContext(), AppDetailActivity::class.java).apply {
            putExtra(AppDetailActivity.APP_DETAIL_REQUEST, AppDetailRequest.InstalledPackage(appListData.packageName))
        }
        startActivity(intent)
    }

}
