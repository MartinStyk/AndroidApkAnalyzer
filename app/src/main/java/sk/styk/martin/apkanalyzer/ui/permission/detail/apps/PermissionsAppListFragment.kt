package sk.styk.martin.apkanalyzer.ui.permission.detail.apps

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.databinding.FragmentPermissionAppListBinding
import sk.styk.martin.apkanalyzer.ui.applist.BaseAppListFragment
import sk.styk.martin.apkanalyzer.ui.permission.detail.pager.PermissionDetailFragment
import sk.styk.martin.apkanalyzer.ui.permission.detail.pager.PermissionDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.util.provideViewModel
import sk.styk.martin.apkanalyzer.util.provideViewModelOfParentFragment
import javax.inject.Inject

@AndroidEntryPoint
class PermissionsAppListFragment : BaseAppListFragment<PermissionsAppListViewModel>() {

    @Inject
    lateinit var viewModelFactory: PermissionsAppListViewModel.Factory

    @Inject
    lateinit var parentViewModelFactory: PermissionDetailFragmentViewModel.Factory

    private lateinit var binding: FragmentPermissionAppListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel {
            val parentViewModel: PermissionDetailFragmentViewModel = provideViewModelOfParentFragment {
                parentViewModelFactory.create(requireNotNull(requireArguments().getParcelable(PermissionDetailFragment.ARG_PERMISSIONS_DATA)))
            }
            viewModelFactory.create(parentViewModel, requireArguments().getBoolean(ARG_GRANTED))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPermissionAppListBinding.inflate(LayoutInflater.from(context), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        binding.recyclerViewAppList.doOnPreDraw { startPostponedEnterTransition() }

        binding.viewModel = viewModel
    }

    override fun fragmentManager() = requireParentFragment().parentFragmentManager

    companion object {
        private const val ARG_GRANTED = "arg_granted"
        fun newInstance(granted: Boolean) = PermissionsAppListFragment().apply {
            arguments = Bundle().apply {
                putBoolean(ARG_GRANTED, granted)
            }
        }
    }
}
