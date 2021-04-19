package sk.styk.martin.apkanalyzer.ui.permission.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import sk.styk.martin.apkanalyzer.databinding.FragmentPermissionListBinding
import sk.styk.martin.apkanalyzer.model.permissions.LocalPermissionData
import sk.styk.martin.apkanalyzer.ui.permission.detail.PermissionDetailActivity
import sk.styk.martin.apkanalyzer.ui.permission.detail.pager.PermissionDetailFragment

@AndroidEntryPoint
class PermissionListFragment : Fragment() {

    private lateinit var binding: FragmentPermissionListBinding

    private val viewModel: PermissionListFragmentViewModel by viewModels()

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
