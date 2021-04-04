package sk.styk.martin.apkanalyzer.ui.permission.detail.apps


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dagger.android.support.AndroidSupportInjection
import sk.styk.martin.apkanalyzer.databinding.FragmentPermissionAppListBinding
import sk.styk.martin.apkanalyzer.ui.applist.BaseAppListFragment
import sk.styk.martin.apkanalyzer.ui.permission.detail.pager.PermissionDetailFragment
import sk.styk.martin.apkanalyzer.ui.permission.detail.pager.PermissionDetailFragmentViewModel
import sk.styk.martin.apkanalyzer.util.provideViewModel
import sk.styk.martin.apkanalyzer.util.provideViewModelOfParentFragment
import javax.inject.Inject


class PermissionsAppListFragment : BaseAppListFragment<PermissionsAppListViewModel>() {

    @Inject
    lateinit var viewModelFactory: PermissionsAppListViewModel.Factory

    @Inject
    lateinit var parentViewModelFactory: PermissionDetailFragmentViewModel.Factory

    private lateinit var binding: FragmentPermissionAppListBinding

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel {
            viewModelFactory.create(
                    provideViewModelOfParentFragment {
                        parentViewModelFactory.create(requireNotNull(requireArguments().getParcelable(PermissionDetailFragment.ARG_PERMISSIONS_DATA)))
                    },
                    requireArguments().getBoolean(ARG_GRANTED)
            )
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPermissionAppListBinding.inflate(LayoutInflater.from(context), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
    }

    companion object {
        private const val ARG_GRANTED = "arg_granted"
        fun newInstance(granted: Boolean) = PermissionsAppListFragment().apply {
            arguments = Bundle().apply {
                putBoolean(ARG_GRANTED, granted)
            }
        }
    }

}
