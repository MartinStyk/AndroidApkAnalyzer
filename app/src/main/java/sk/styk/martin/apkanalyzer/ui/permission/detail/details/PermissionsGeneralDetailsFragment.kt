package sk.styk.martin.apkanalyzer.ui.permission.detail.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.databinding.FragmentPermissionDetailGeneralBinding
import sk.styk.martin.apkanalyzer.ui.permission.detail.pager.PermissionDetailFragment
import sk.styk.martin.apkanalyzer.ui.permission.detail.pager.PermissionDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.util.components.toDialog
import sk.styk.martin.apkanalyzer.util.components.toSnackbar
import sk.styk.martin.apkanalyzer.util.provideViewModel
import sk.styk.martin.apkanalyzer.util.provideViewModelOfParentFragment
import javax.inject.Inject

@AndroidEntryPoint
class PermissionsGeneralDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: PermissionsGeneralDetailsViewModel.Factory

    @Inject
    lateinit var parentViewModelFactory: PermissionDetailFragmentViewModel.Factory

    private lateinit var binding: FragmentPermissionDetailGeneralBinding

    private lateinit var viewModel: PermissionsGeneralDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel {
            viewModelFactory.create(
                provideViewModelOfParentFragment {
                    parentViewModelFactory.create(requireNotNull(requireArguments().getParcelable(PermissionDetailFragment.ARG_PERMISSIONS_DATA)))
                },
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPermissionDetailGeneralBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        with(viewModel) {
            openDescription.observe(viewLifecycleOwner) {
                it.toDialog().show(parentFragmentManager, "descrition_dialog")
            }
            showSnackbar.observe(viewLifecycleOwner) {
                it.toSnackbar(
                    requireActivity().findViewById(
                        android.R.id.content,
                    ),
                ).show()
            }
        }
    }
}
