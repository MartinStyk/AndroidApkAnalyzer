package sk.styk.martin.apkanalyzer.ui.appdetail.page.general

import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.databinding.FragmentAppDetailPageBinding
import sk.styk.martin.apkanalyzer.ui.appdetail.page.AppDetailPageFragment
import sk.styk.martin.apkanalyzer.util.provideViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AppGeneralDetailFragment : AppDetailPageFragment<AppGeneralDetailsFragmentViewModel, FragmentAppDetailPageBinding>() {

    @Inject
    lateinit var viewModelFactory: AppGeneralDetailsFragmentViewModel.Factory

    override fun createViewModel(): AppGeneralDetailsFragmentViewModel {
        return provideViewModel {
            viewModelFactory.create(parentViewModel())
        }
    }

    override fun createFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentAppDetailPageBinding {
        return FragmentAppDetailPageBinding.inflate(inflater, container, false)
    }
}
