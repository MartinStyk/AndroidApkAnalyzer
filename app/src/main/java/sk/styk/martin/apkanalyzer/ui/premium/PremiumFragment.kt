package sk.styk.martin.apkanalyzer.ui.premium

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import sk.styk.martin.apkanalyzer.databinding.FragmentPremiumBinding
import sk.styk.martin.apkanalyzer.dependencyinjection.viewmodel.ViewModelFactory
import sk.styk.martin.apkanalyzer.util.file.AppOperations
import sk.styk.martin.apkanalyzer.util.provideViewModel
import javax.inject.Inject

class PremiumFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentPremiumBinding

    private lateinit var viewModel: PremiumFragmentViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel(viewModelFactory)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPremiumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        viewModel.openGooglePlay.observe(viewLifecycleOwner, { AppOperations.openGooglePlay(requireContext(), it) })
    }

}
