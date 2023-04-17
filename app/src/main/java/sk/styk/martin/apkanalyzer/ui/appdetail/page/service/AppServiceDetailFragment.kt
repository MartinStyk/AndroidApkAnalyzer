package sk.styk.martin.apkanalyzer.ui.appdetail.page.service

import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.databinding.FragmentAppDetailPageBinding
import sk.styk.martin.apkanalyzer.ui.appdetail.page.AppDetailPageFragment
import sk.styk.martin.apkanalyzer.util.provideViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AppServiceDetailFragment : AppDetailPageFragment<AppServiceDetailFragmentViewModel, FragmentAppDetailPageBinding>() {

    @Inject
    lateinit var viewModelFactory: AppServiceDetailFragmentViewModel.Factory

    override fun createViewModel(): AppServiceDetailFragmentViewModel {
        return provideViewModel {
            viewModelFactory.create(parentViewModel())
        }
    }

    override fun createFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentAppDetailPageBinding {
        return FragmentAppDetailPageBinding.inflate(inflater, container, false)
    }
}
