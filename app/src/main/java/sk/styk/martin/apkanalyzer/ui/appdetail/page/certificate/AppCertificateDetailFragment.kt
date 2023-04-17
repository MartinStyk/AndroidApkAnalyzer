package sk.styk.martin.apkanalyzer.ui.appdetail.page.certificate

import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.databinding.FragmentAppDetailPageBinding
import sk.styk.martin.apkanalyzer.ui.appdetail.page.AppDetailPageFragment
import sk.styk.martin.apkanalyzer.util.provideViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AppCertificateDetailFragment : AppDetailPageFragment<AppCertificateDetailsFragmentViewModel, FragmentAppDetailPageBinding>() {

    @Inject
    lateinit var viewModelFactory: AppCertificateDetailsFragmentViewModel.Factory

    override fun createViewModel(): AppCertificateDetailsFragmentViewModel {
        return provideViewModel {
            viewModelFactory.create(parentViewModel())
        }
    }

    override fun createFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentAppDetailPageBinding {
        return FragmentAppDetailPageBinding.inflate(inflater, container, false)
    }
}
