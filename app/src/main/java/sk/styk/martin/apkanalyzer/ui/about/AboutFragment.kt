package sk.styk.martin.apkanalyzer.ui.about

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.databinding.FragmentAboutBinding
import sk.styk.martin.apkanalyzer.util.file.AppOperations
import sk.styk.martin.apkanalyzer.util.provideViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AboutFragment : Fragment() {

    @Inject
    lateinit var factory: AboutFragmentViewModel.Factory

    private lateinit var binding: FragmentAboutBinding

    private lateinit var viewModel: AboutFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel { factory.create() }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentAboutBinding.inflate(inflater, container, false)
        binding.aboutAppGithubLink.movementMethod = LinkMovementMethod.getInstance()
        binding.aboutAppPrivacyPolicy.movementMethod = LinkMovementMethod.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        viewModel.openGooglePlay.observe(viewLifecycleOwner) {
            AppOperations.openGooglePlay(
                requireContext(),
                requireContext().packageName,
            )
        }
    }
}
