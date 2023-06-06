package sk.styk.martin.apkanalyzer.ui.permission.detail.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.databinding.FragmentPermissionDetailBinding
import sk.styk.martin.apkanalyzer.core.apppermissions.model.LocalPermissionData
import sk.styk.martin.apkanalyzer.util.components.toDialog
import sk.styk.martin.apkanalyzer.util.materialContainerTransform
import sk.styk.martin.apkanalyzer.util.provideViewModel
import javax.inject.Inject

@AndroidEntryPoint
class PermissionDetailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: PermissionDetailFragmentViewModel.Factory

    private lateinit var binding: FragmentPermissionDetailBinding

    private lateinit var viewModel: PermissionDetailFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = requireContext().materialContainerTransform()
        viewModel = provideViewModel { viewModelFactory.create(requireNotNull(requireArguments().getParcelable(ARG_PERMISSIONS_DATA))) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPermissionDetailBinding.inflate(inflater, container, false)
        binding.pager.adapter = PermissionDetailPagerAdapter(requireArguments(), requireContext().applicationContext, childFragmentManager)
        binding.tabs.setupWithViewPager(binding.pager)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel

        with(viewModel) {
            showDialog.observe(viewLifecycleOwner) {
                it.toDialog().show(parentFragmentManager, "PermissionDescription")
            }
            close.observe(viewLifecycleOwner) { requireActivity().onBackPressed() }
        }
    }

    companion object {

        const val ARG_PERMISSIONS_DATA = "permission_args"

        fun create(permissionData: LocalPermissionData) = PermissionDetailFragment().apply {
            arguments = Bundle().apply {
                putParcelable(ARG_PERMISSIONS_DATA, permissionData)
            }
        }
    }
}
