package sk.styk.martin.apkanalyzer.ui.appdetail.page.usedpermission

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.databinding.FragmentAppDetailPageBinding
import sk.styk.martin.apkanalyzer.ui.appdetail.page.AppDetailPageFragment
import sk.styk.martin.apkanalyzer.ui.appdetail.page.definedpermission.AppDefinedPermissionFragmentViewModel
import sk.styk.martin.apkanalyzer.util.components.toDialog
import sk.styk.martin.apkanalyzer.util.provideViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AppDefinedPermissionDetailFragment : AppDetailPageFragment<AppDefinedPermissionFragmentViewModel, FragmentAppDetailPageBinding>() {

    @Inject
    lateinit var viewModelFactory: AppDefinedPermissionFragmentViewModel.Factory

    override fun createViewModel(): AppDefinedPermissionFragmentViewModel {
        return provideViewModel {
            viewModelFactory.create(parentViewModel())
        }
    }

    override fun createFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentAppDetailPageBinding {
        return FragmentAppDetailPageBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.showDialog.observe(viewLifecycleOwner) {
            it.toDialog().show(parentFragmentManager, "permission_description")
        }
    }
}
