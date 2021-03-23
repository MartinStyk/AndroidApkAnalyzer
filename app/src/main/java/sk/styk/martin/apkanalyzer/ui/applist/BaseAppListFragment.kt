package sk.styk.martin.apkanalyzer.ui.applist


import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import sk.styk.martin.apkanalyzer.ui.activity.appdetail.base.AppDetailActivity

abstract class BaseAppListFragment<VM: BaseAppListViewModel> : Fragment() {

    protected lateinit var viewModel: VM

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.appClicked.observe(viewLifecycleOwner, { startActivity(AppDetailActivity.createIntent(packageName = it.packageName, context = requireContext())) })
    }

}
