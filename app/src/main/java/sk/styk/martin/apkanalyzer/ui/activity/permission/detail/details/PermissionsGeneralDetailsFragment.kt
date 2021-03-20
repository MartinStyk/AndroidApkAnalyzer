package sk.styk.martin.apkanalyzer.ui.activity.permission.detail.details


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import sk.styk.martin.apkanalyzer.databinding.FragmentPermissionDetailGeneralBinding
import sk.styk.martin.apkanalyzer.dependencyinjection.viewmodel.ViewModelFactory
import sk.styk.martin.apkanalyzer.util.provideViewModel
import sk.styk.martin.apkanalyzer.util.provideViewModelOfParentFragment
import javax.inject.Inject

class PermissionsGeneralDetailsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: PermissionsGeneralDetailsViewModel.Factory

    @Inject
    lateinit var viewModelFactory2: ViewModelFactory

    private lateinit var binding: FragmentPermissionDetailGeneralBinding

    private lateinit var viewModel: PermissionsGeneralDetailsViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel {
            viewModelFactory.create(provideViewModelOfParentFragment())
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
    }

}
