package sk.styk.martin.apkanalyzer.ui.appdetail.page.certificate

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import sk.styk.martin.apkanalyzer.databinding.FragmentAppDetailPageBinding
import sk.styk.martin.apkanalyzer.ui.appdetail.page.AppDetailPageFragment
import sk.styk.martin.apkanalyzer.util.provideViewModel
import javax.inject.Inject

class AppCertificateDetailFragment : AppDetailPageFragment<AppCertificateDetailsFragmentViewModel, FragmentAppDetailPageBinding>() {

    @Inject
    lateinit var viewModelFactory: AppCertificateDetailsFragmentViewModel.Factory

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun createViewModel(): AppCertificateDetailsFragmentViewModel {
        return provideViewModel {
            viewModelFactory.create(parentViewModel())
        }
    }

    override fun createFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentAppDetailPageBinding {
        return FragmentAppDetailPageBinding.inflate(inflater, container, false)
    }

}

