package sk.styk.martin.apkanalyzer.ui.permission.list

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.android.support.AndroidSupportInjection
import sk.styk.martin.apkanalyzer.databinding.FragmentPermissionListBinding
import sk.styk.martin.apkanalyzer.dependencyinjection.viewmodel.ViewModelFactory
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData
import sk.styk.martin.apkanalyzer.ui.activity.permission.detail.PermissionDetailActivity
import sk.styk.martin.apkanalyzer.ui.activity.permission.detail.pager.PermissionDetailFragment
import sk.styk.martin.apkanalyzer.util.provideViewModel
import javax.inject.Inject

class PermissionListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var binding: FragmentPermissionListBinding

    private lateinit var viewModel: PermissionListFragmentViewModel

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel(viewModelFactory)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentPermissionListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel

        viewModel.openPermission.observe(viewLifecycleOwner, { openPermissionDetail(it) })
    }

    private fun openPermissionDetail(permission: LocalPermissionData) {
        val intent = Intent(context, PermissionDetailActivity::class.java)
        intent.putExtra(PermissionDetailFragment.ARG_PERMISSIONS_DATA, permission)
        requireContext().startActivity(intent)
    }

}
