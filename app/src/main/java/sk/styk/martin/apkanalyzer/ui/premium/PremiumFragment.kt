package sk.styk.martin.apkanalyzer.ui.premium

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.databinding.FragmentPremiumBinding
import sk.styk.martin.apkanalyzer.util.file.AppOperations

@AndroidEntryPoint
class PremiumFragment : Fragment() {

    private lateinit var binding: FragmentPremiumBinding

    private val viewModel: PremiumFragmentViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPremiumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        viewModel.openGooglePlay.observe(viewLifecycleOwner) {
            AppOperations.openGooglePlay(
                requireContext(),
                it,
            )
        }
    }
}
