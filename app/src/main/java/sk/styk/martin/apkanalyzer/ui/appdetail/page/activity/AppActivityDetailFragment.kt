package sk.styk.martin.apkanalyzer.ui.appdetail.page.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.databinding.FragmentAppDetailPageBinding
import sk.styk.martin.apkanalyzer.ui.appdetail.page.AppDetailPageFragment
import sk.styk.martin.apkanalyzer.util.file.AppOperations
import sk.styk.martin.apkanalyzer.util.provideViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AppActivityDetailFragment : AppDetailPageFragment<AppActivityDetailFragmentViewModel, FragmentAppDetailPageBinding>() {

    @Inject
    lateinit var viewModelFactory: AppActivityDetailFragmentViewModel.Factory

    override fun createViewModel(): AppActivityDetailFragmentViewModel {
        return provideViewModel {
            viewModelFactory.create(parentViewModel())
        }
    }

    override fun createFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentAppDetailPageBinding {
        return FragmentAppDetailPageBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewModel) {
            runActivity.observe(viewLifecycleOwner) {
                startForeignActivity(
                    it.packageName,
                    it.name,
                )
            }
        }
    }

    private fun startForeignActivity(packageName: String, activityName: String) {
        AppOperations.startForeignActivity(requireActivity(), packageName, activityName)
    }
}
